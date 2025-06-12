package org.example.korkiedp.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.korkiedp.app.MessagePopupManager;
import org.example.korkiedp.async.BackgroundWorker;
import org.example.korkiedp.component.InfoMessageController;
import org.example.korkiedp.dao.LessonDAO;
import org.example.korkiedp.dao.TutorStudentDAO;
import org.example.korkiedp.events.EventBus;
import org.example.korkiedp.events.LessonSelectedEvent;
import org.example.korkiedp.events.generalInfoForTutorGatheredEvent;
import org.example.korkiedp.model.Lesson;
import org.example.korkiedp.model.TutorStudent;
import org.example.korkiedp.service.SceneSwitcherService;
import org.example.korkiedp.service.WeatherService;
import org.example.korkiedp.session.CurrentSession;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartMenuController {
    public ComboBox<String> attendanceComboBox;
    public ComboBox<String> cancellationReasonComboBox;
    public ComboBox<String> paymentComboBox;
    public TextField durationTextField;
    public Label weatherLabel;
    @FXML private TableColumn<Lesson, String> topicColumn;
    @FXML private TableColumn<Lesson, String> priceColumn;
    @FXML private TableColumn<Lesson, String> studentColumn;
    @FXML private TableColumn<Lesson, String> dateColumn;
    @FXML private TableColumn<Lesson, String> timeColumn;
    public TextField priceTextField;
    public HBox cancellationReasonBox;
    public TextField customReasonTextField;
    public Button saveLessonButton;
    public VBox lessonEditBox;
    @FXML private TableView<Lesson> lessonsTable;
    @FXML private VBox formBox;
    @FXML private Label formMessageLabel;

    @FXML private Label activeStudentsInfoLabel;
    @FXML private Label lessonsAmountInfoLabel;
    @FXML private Label earningsAmountInfoLabel;
    private int activeStudents;
    int lessonsAmount;
    BigDecimal earningsAmount;


    @FXML private Button newRelationButton;
    @FXML private Button newLessonButton;

    @FXML private HBox customReasonBox;
    private final ObservableList<Lesson> lessonList = FXCollections.observableArrayList();
    private final Map<Integer, String> studentNames = new HashMap<>();
    private Lesson selectedLesson;

    public void initialize() {
        BackgroundWorker.submit(() -> {
            String weather = WeatherService.getWeather(52.237049, 21.017532);
            Platform.runLater(() -> weatherLabel.setText("Pogoda: " + weather));
        });
        lessonsTable.setPlaceholder(new Label("Wczytywanie..."));
        EventBus.subscribe(generalInfoForTutorGatheredEvent.class,(event) -> Platform.runLater(()-> setInfoLabel(event)));
        EventBus.subscribe(LessonSelectedEvent.class,(event) -> Platform.runLater(() -> setLessonFields(event)));

        attendanceComboBox.setOnAction(e -> {
            boolean isAbsent = "Nie".equals(attendanceComboBox.getValue());
            cancellationReasonBox.setVisible(isAbsent);
            cancellationReasonBox.setManaged(isAbsent);

            // Reset cancellation reason if not absent
            if (!isAbsent) {
                cancellationReasonComboBox.setValue(null);
                customReasonBox.setVisible(false);
                customReasonBox.setManaged(false);
                customReasonTextField.clear();
            }
        });

        BackgroundWorker.submit(()->{
            activeStudents = TutorStudentDAO.countActiveStudentsByTutorId(CurrentSession.getTutorId());
            lessonsAmount = LessonDAO.countFinishedLessons(CurrentSession.getTutorId());
            earningsAmount = LessonDAO.sumFinishedLessonsPrice(CurrentSession.getTutorId());


            EventBus.publish(new generalInfoForTutorGatheredEvent(activeStudents, lessonsAmount, earningsAmount));
        });
        BackgroundWorker.submit(() -> {
            loadStudentNames();

            List<Lesson> finishedLessons = LessonDAO.findFinishedLessonsByTutorId(CurrentSession.getTutorId());
            if(finishedLessons.isEmpty()) Platform.runLater(()-> lessonsTable.setPlaceholder(new Label("Brak zakończonych nieopłaconych lekcji.")));

            Platform.runLater(() -> {
                setupTableColumns();
                finishedLessons.sort((l1, l2) -> {
                    int dateCompare = l1.getDate().compareTo(l2.getDate());
                    if (dateCompare == 0) {
                        return l1.getStartTime().compareTo(l2.getStartTime()); // DESC for time
                    }
                    return dateCompare; // ASC for date
                });
                lessonList.setAll(finishedLessons);
            });
        });
        lessonsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lessonEditBox.setVisible(true);
                lessonEditBox.setManaged(true);

                selectedLesson = newVal;
                EventBus.publish(new LessonSelectedEvent(selectedLesson));
            } else {
                lessonEditBox.setVisible(false);
                lessonEditBox.setManaged(false);
            }
        });


        cancellationReasonComboBox.setOnAction(e -> {
            String selected = cancellationReasonComboBox.getValue();
            boolean showCustom = "Inny powód".equals(selected);
            customReasonBox.setVisible(showCustom);
            customReasonBox.setManaged(showCustom);
        });
    }

    private void setInfoLabel(generalInfoForTutorGatheredEvent event) {
        activeStudentsInfoLabel.setText(String.valueOf(activeStudents));
        lessonsAmountInfoLabel.setText(String.valueOf(lessonsAmount));
        earningsAmountInfoLabel.setText(String.valueOf(earningsAmount));
    }
    private void loadStudentNames() {
        List<TutorStudent> students = TutorStudentDAO.findByTutorId(CurrentSession.getTutorId());
        for (TutorStudent ts : students) {
            studentNames.put(ts.getStudentId(), ts.getDefault_name());
        }
    }
    private void setupTableColumns() {
        lessonsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        timeColumn.prefWidthProperty().bind(lessonsTable.widthProperty().multiply(0.15));
        dateColumn.prefWidthProperty().bind(lessonsTable.widthProperty().multiply(0.2));
        studentColumn.prefWidthProperty().bind(lessonsTable.widthProperty().multiply(0.25));
        topicColumn.prefWidthProperty().bind(lessonsTable.widthProperty().multiply(0.25));
        priceColumn.prefWidthProperty().bind(lessonsTable.widthProperty().multiply(0.15));
        timeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"))));

        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));

        studentColumn.setCellValueFactory(cellData -> {
            int id = cellData.getValue().getStudentId();
            return new SimpleStringProperty(studentNames.getOrDefault(id, "ID: " + id));
        });

        topicColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSubject()));

        priceColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPrice().toPlainString() + " zł"));

        lessonsTable.setItems(lessonList);
    }

    private void setLessonFields(LessonSelectedEvent event){
        durationTextField.setText(Integer.toString(event.getLesson().getDurationMinutes()));
        priceTextField.setText(event.getLesson().getPrice().toString());

        attendanceComboBox.setValue(event.getLesson().getAttendance() != null ? (event.getLesson().getAttendance() ? "Tak" : "Nie") : null);
        paymentComboBox.setValue(event.getLesson().isPaid() ? "Tak" : "Nie");
        cancellationReasonComboBox.setValue(event.getLesson().getCancelReason());

        cancellationReasonBox.setVisible(false);
        cancellationReasonBox.setManaged(false);

        boolean isCustom = "Inny powód".equals(event.getLesson().getCancelReason());
        customReasonBox.setVisible(isCustom);
        customReasonBox.setManaged(isCustom);
        customReasonTextField.setText(isCustom ? event.getLesson().getCancelReason() : "");

    }
    private void refreshLessonTable() {
        BackgroundWorker.submit(() -> {
            List<Lesson> updatedLessons = LessonDAO.findFinishedLessonsByTutorId(CurrentSession.getTutorId());
            updatedLessons.sort((l1, l2) -> {
                int dateCompare = l1.getDate().compareTo(l2.getDate());
                if (dateCompare == 0) {
                    return l1.getStartTime().compareTo(l2.getStartTime());
                }
                return dateCompare;
            });

            Platform.runLater(() -> {
                lessonList.setAll(updatedLessons);
                lessonsTable.refresh();
            });
        });
    }


    @FXML
    private void handleSave() {
        if (selectedLesson == null) {
            MessagePopupManager.sendPopup("Nie wybrano lekcji!", InfoMessageController.MessageType.ERROR);
            return;
        }

        String durationText = durationTextField.getText();
        String priceText = priceTextField.getText();

        if (durationText == null || durationText.isBlank()) {
            MessagePopupManager.sendPopup("Podaj długość lekcji!", InfoMessageController.MessageType.ERROR);
            return;
        }

        if (priceText == null || priceText.isBlank()) {
            MessagePopupManager.sendPopup("Podaj cenę!", InfoMessageController.MessageType.ERROR);
            return;
        }

        try {
            int duration = Integer.parseInt(durationText);
            if (duration <= 0) {
                MessagePopupManager.sendPopup("Czas trwania musi być większy niż 0!", InfoMessageController.MessageType.ERROR);
                return;
            }

            BigDecimal price = new BigDecimal(priceText);
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                MessagePopupManager.sendPopup("Cena musi być większa niż 0!", InfoMessageController.MessageType.ERROR);
                return;
            }

            selectedLesson.setDurationMinutes(duration);
            selectedLesson.setPrice(price);

            // Attendance & cancellation logic
            boolean attended = "Tak".equals(attendanceComboBox.getValue());
            selectedLesson.setAttendance(attended);
            selectedLesson.setCanceled(!attended);

            String selectedReason = cancellationReasonComboBox.getValue();
            if (!attended) {
                if (selectedReason == null || selectedReason.isBlank()) {
                    MessagePopupManager.sendPopup("Wybierz powód odwołania!", InfoMessageController.MessageType.ERROR);
                    return;
                }

                if ("Inny powód".equals(selectedReason)) {
                    String customReason = customReasonTextField.getText();
                    if (customReason == null || customReason.isBlank()) {
                        MessagePopupManager.sendPopup("Wpisz powód odwołania!", InfoMessageController.MessageType.ERROR);
                        return;
                    }
                    selectedLesson.setCancelReason(customReason);
                } else {
                    selectedLesson.setCancelReason(selectedReason);
                }
            } else {
                selectedLesson.setCancelReason(null);
            }

            // Payment status (optional logic)
            if ("Tak".equals(paymentComboBox.getValue())) {
                selectedLesson.setPaid(true);
            } else if ("Nie".equals(paymentComboBox.getValue())) {
                selectedLesson.setPaid(false);
            }

            // Save to DB
            BackgroundWorker.submit(() -> {
                LessonDAO.update(selectedLesson); // assumes you have an update method
                MessagePopupManager.sendPopup("Zapisano zmiany", InfoMessageController.MessageType.SUCCESS);
                Platform.runLater(() -> refreshLessonTable());
            });

        } catch (NumberFormatException e) {
            MessagePopupManager.sendPopup("Nieprawidłowy format liczbowy!", InfoMessageController.MessageType.ERROR);
        } catch (Exception e) {
            MessagePopupManager.sendPopup("Błąd zapisu: " + e.getMessage(), InfoMessageController.MessageType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    public void newLesson(ActionEvent event) {
        SceneSwitcherService.loadMainPanel("/newLesson.fxml");
    }

    @FXML
    public void newRelation(ActionEvent event) {
        SceneSwitcherService.loadMainPanel("/newRelationView.fxml");
    }
}
