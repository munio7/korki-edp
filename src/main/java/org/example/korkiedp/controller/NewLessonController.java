package org.example.korkiedp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.example.korkiedp.app.MessagePopupManager;
import org.example.korkiedp.async.DbWorker;
import org.example.korkiedp.component.InfoMessageController;
import org.example.korkiedp.dao.LessonDAO;
import org.example.korkiedp.dao.TutorStudentDAO;
import org.example.korkiedp.events.*;
import org.example.korkiedp.model.Lesson;
import org.example.korkiedp.model.Tutor;
import org.example.korkiedp.model.TutorStudent;
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
    @FXML private TextField hourField;
    @FXML private TextField minuteField;
    @FXML private ComboBox<String> studentComboBox;

    private List<TutorStudent> tutorStudents;
    private Map<String,TutorStudent> studentsName = new HashMap<>();

    @FXML
    private void initialize() {
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
        });

        DbWorker.submit( () -> {
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

    public Map<String, Integer>  setStudentsMap(List<TutorStudent> fromDb) {
        Map<String, Integer> map = new HashMap<>();
        for (TutorStudent ts : fromDb) {
            map.put(ts.getDefault_name() ,ts.getStudentId());
        }
        return map;
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

    public void handleSave(ActionEvent event) {
        if (studentComboBox.getValue() == null || studentComboBox.getValue().isBlank()) {
            MessagePopupManager.sendPopup("Wybierz studenta!", InfoMessageController.MessageType.ERROR);
            return;
        }
        if (priceField.getText() == null || priceField.getText().isBlank()) {
            MessagePopupManager.sendPopup("Ustal cenę!", InfoMessageController.MessageType.ERROR);
            return;
        }
        if (subjectComboBox.getValue() == null || subjectComboBox.getValue().isBlank()) {
            MessagePopupManager.sendPopup("Wybierz przedmiot!", InfoMessageController.MessageType.ERROR);
            return;
        }
        if (datePicker.getValue() == null) {
            MessagePopupManager.sendPopup("Wybierz date!", InfoMessageController.MessageType.ERROR);
            return;
        }
        if (minuteField.getText() == null || minuteField.getText().isBlank() || hourField.getText() == null || hourField.getText().isBlank()) {
            MessagePopupManager.sendPopup("Wybierz godzinę!", InfoMessageController.MessageType.ERROR);
            return;
        }

        BigDecimal hourlyPrice = new BigDecimal(priceField.getText());
        int durationMinutes = parseInt(durationField.getText());
        BigDecimal finalPrice = hourlyPrice
                .multiply(BigDecimal.valueOf(durationMinutes)).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        Lesson lesson = new Lesson(
                CurrentSession.getTutorId(),
                studentsName.get(studentComboBox.getValue()).getStudentId(),
                datePicker.getValue(),
                LocalTime.of(parseInt(hourField.getText()), parseInt(minuteField.getText())),
                durationMinutes,
                finalPrice,
                topicField.getText(),
                subjectComboBox.getValue()
        );

        DbWorker.submit( () -> {
            boolean status =  LessonDAO.save(lesson);
            EventBus.publish(new newLessonEvent(status));
        });
    }

    private void goBack(){
        SceneSwitcherService.loadMainPanel("/calendar.fxml");
    }

}

