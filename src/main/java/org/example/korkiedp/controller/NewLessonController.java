package org.example.korkiedp.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.example.korkiedp.app.MessagePopupManager;
import org.example.korkiedp.async.BackgroundWorker;
import org.example.korkiedp.component.InfoMessageController;
import org.example.korkiedp.dao.LessonDAO;
import org.example.korkiedp.dao.TutorStudentDAO;
import org.example.korkiedp.events.*;
import org.example.korkiedp.model.CurrencyCode;
import org.example.korkiedp.model.Lesson;
import org.example.korkiedp.model.TutorStudent;
import org.example.korkiedp.service.CurrencyService;
import org.example.korkiedp.service.SceneSwitcherService;
import org.example.korkiedp.session.CurrentSession;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewLessonController {

    public ComboBox<String> subjectComboBox;
    public DatePicker datePicker;
    public TextField durationField;
    public TextField priceField;
    public TextField topicField;
    @FXML private ComboBox<CurrencyCode> currencyComboBox;
    @FXML private TextField hourField;
    @FXML private TextField minuteField;
    @FXML private ComboBox<String> studentComboBox;

    private List<TutorStudent> tutorStudents;
    private Map<String,TutorStudent> studentsName = new HashMap<>();
    private CurrencyCode lastSelectedCurrency = CurrencyCode.PLN;


    @FXML
    private void initialize() {
        currencyComboBox.getItems().setAll(CurrencyCode.values());
        currencyComboBox.setValue(CurrencyCode.PLN); // default if needed
        EventBus.subscribe(allTutorsStudentsFoundEvent.class, event -> setStudentsComboBox(event));
        EventBus.subscribe(newLessonEvent.class, event -> {
            if (event.getStatus()) {
                MessagePopupManager.sendPopup("Pomyślnie utworzono lekcję!", InfoMessageController.MessageType.SUCCESS);
                goBack();
            } else {
                MessagePopupManager.sendPopup("Nie udało się uwtowrzyć lekcji.", InfoMessageController.MessageType.ERROR);
            }
        });

        studentComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            priceField.setText(studentsName.get(newValue).getDefault_price().toString());
            lastSelectedCurrency = CurrencyCode.PLN;
            currencyComboBox.setValue(lastSelectedCurrency);
        });
        currencyComboBox.valueProperty().addListener((obs, oldVal, newVal) -> recalculatePrice());

        BackgroundWorker.submit( () -> {
            List<TutorStudent> fromDb = TutorStudentDAO.findByTutorId(CurrentSession.getTutorId());
            tutorStudents = fromDb;
            EventBus.publish(new allTutorsStudentsFoundEvent(fromDb));
        });

        subjectComboBox.getItems().addAll(
                "Matematyka",
                "Fizyka",
                "Chemia",
                "Biologia",
                "Angielski",
                "Polski",
                "Informatyka"
        );
        setTimeField(hourField);
        setTimeField(minuteField);
    }

    public void setSelectedDate(LocalDate date) {
        datePicker.setValue(date);
    }

    private void setStudentsComboBox(allTutorsStudentsFoundEvent event) {
        Map<String, TutorStudent> map = new HashMap<>();
        for (TutorStudent ts : event.getFromDb()) {
            String name = ts.getDefault_name();
            map.put(name, ts);
            studentComboBox.getItems().add(name);
        }
        studentsName = map;
    }

    private void setTimeField(TextField timeField) {
        timeField.setTextFormatter(new javafx.scene.control.TextFormatter<>(change -> {
            if (change.getText().matches("[0-9]*")) {
                return change;
            }
            if ((change.getControlNewText().length() > 2)) {
                return null;
            }
            return null;
        }));
    }

    private void recalculatePrice() {
        CurrencyCode newCurrency = currencyComboBox.getValue();

        // Jeśli to pierwsze ustawienie lub ta sama waluta, nic nie rób
        if (newCurrency == lastSelectedCurrency) return;

        String priceText = priceField.getText();
        if (priceText == null || priceText.isBlank()) return;

        BigDecimal currentValue;
        try {
            currentValue = new BigDecimal(priceText);
        } catch (NumberFormatException e) {
            Platform.runLater(() ->
                    MessagePopupManager.sendPopup("Nieprawidłowa cena – wpisz liczbę.", InfoMessageController.MessageType.ERROR)
            );
            return;
        }

        BackgroundWorker.submit(() -> {
            double oldRate = lastSelectedCurrency == CurrencyCode.PLN ? 1.0 : CurrencyService.getExchangeRate(lastSelectedCurrency.name());
            double newRate = newCurrency == CurrencyCode.PLN ? 1.0 : CurrencyService.getExchangeRate(newCurrency.name());

            if (oldRate == -1 || newRate == -1) {
                Platform.runLater(() ->
                        MessagePopupManager.sendPopup("Nie udało się pobrać kursu waluty.", InfoMessageController.MessageType.ERROR)
                );
                return;
            }

            BigDecimal plnValue = currentValue.multiply(BigDecimal.valueOf(oldRate));
            BigDecimal newValue = plnValue.divide(BigDecimal.valueOf(newRate), 2, RoundingMode.HALF_UP);

            Platform.runLater(() -> {
                priceField.setText(newValue.toPlainString());
                lastSelectedCurrency = newCurrency;
            });
        });
    }

    @FXML
    private void incrementHour() {
        int h = parseInt(hourField.getText());
        hourField.setText(String.format("%02d", (h + 1) % 24));
    }

    @FXML
    private void decrementHour() {
        int h = parseInt(hourField.getText());
        hourField.setText(String.format("%02d", (h - 1 + 24) % 24));
    }

    @FXML
    private void incrementMinutes() {
        int m = parseInt(minuteField.getText());
        m += 15;
        if (m >= 60) m -= 60;
        minuteField.setText(String.format("%02d", m));
    }

    @FXML
    private void decrementMinutes() {
        int m = parseInt(minuteField.getText());
        m -= 15;
        if (m < 0) m += 60;
        minuteField.setText(String.format("%02d", m));
    }

    private int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void handleCancel(ActionEvent event) {
        goBack();
    }

    @FXML
    public void handleSave(ActionEvent event) {
        if (studentComboBox.getValue() == null || studentComboBox.getValue().isBlank()) {
            MessagePopupManager.sendPopup("Wybierz studenta!", InfoMessageController.MessageType.ERROR);
            return;
        }
        if (subjectComboBox.getValue() == null || subjectComboBox.getValue().isBlank()) {
            MessagePopupManager.sendPopup("Wybierz przedmiot!", InfoMessageController.MessageType.ERROR);
            return;
        }
        if (datePicker.getValue() == null) {
            MessagePopupManager.sendPopup("Wybierz datę!", InfoMessageController.MessageType.ERROR);
            return;
        }
        if (minuteField.getText().isBlank() || hourField.getText().isBlank()) {
            MessagePopupManager.sendPopup("Wybierz godzinę!", InfoMessageController.MessageType.ERROR);
            return;
        }
        if (durationField.getText().isBlank()) {
            MessagePopupManager.sendPopup("Określ długość lekcji!", InfoMessageController.MessageType.ERROR);
            return;
        }

        String priceText = priceField.getText();
        if (priceText == null || priceText.isBlank()) {
            MessagePopupManager.sendPopup("Ustal cenę!", InfoMessageController.MessageType.ERROR);
            return;
        }

        int durationMinutes = parseInt(durationField.getText());
        CurrencyCode selectedCurrency = currencyComboBox.getValue();

        BackgroundWorker.submit(() -> {
            double rate = selectedCurrency == CurrencyCode.PLN ? 1.0 : CurrencyService.getExchangeRate(selectedCurrency.name());
            if (rate == -1) {
                Platform.runLater(() ->
                        MessagePopupManager.sendPopup("Nie udało się pobrać kursu waluty.", InfoMessageController.MessageType.ERROR)
                );
                return;
            }

            BigDecimal userAmount = new BigDecimal(priceText);
            BigDecimal finalPricePLN = userAmount.multiply(BigDecimal.valueOf(rate)).setScale(2, RoundingMode.HALF_UP);

            Lesson lesson = new Lesson(
                    CurrentSession.getTutorId(),
                    studentsName.get(studentComboBox.getValue()).getStudentId(),
                    datePicker.getValue(),
                    LocalTime.of(parseInt(hourField.getText()), parseInt(minuteField.getText())),
                    durationMinutes,
                    finalPricePLN,
                    topicField.getText(),
                    subjectComboBox.getValue()
            );

            boolean status = LessonDAO.save(lesson);
            EventBus.publish(new newLessonEvent(status));
        });
    }


    private void goBack(){
        SceneSwitcherService.loadMainPanel("/calendar.fxml");
    }

}

