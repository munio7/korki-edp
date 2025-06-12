package org.example.korkiedp.component;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.korkiedp.app.MessagePopupManager;
import org.example.korkiedp.async.BackgroundWorker;
import org.example.korkiedp.controller.NewLessonController;
import org.example.korkiedp.dao.LessonDAO;
import org.example.korkiedp.dao.TutorStudentDAO;
import org.example.korkiedp.events.EventBus;
import org.example.korkiedp.events.LessonsForDateFoundEvent;
import org.example.korkiedp.events.newDateSelectedEvent;
import org.example.korkiedp.model.Lesson;
import org.example.korkiedp.model.TutorStudent;
import org.example.korkiedp.service.SceneSwitcherService;
import org.example.korkiedp.session.CurrentSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LessonsComponentController {
    public Label selectedDateLabel;
    @FXML public TableColumn<Lesson, String> subjectColumn;
    public Button deleteButton;
    @FXML
    private TableView<Lesson> lessonsTable;
    @FXML private TableColumn<Lesson, String> timeColumn;
    @FXML private TableColumn<Lesson, String> studentColumn;
    @FXML private TableColumn<Lesson, String> statusColumn;
    private LocalDate selectedDate;
    boolean studentsListSet = false;


    private Map<Integer, String> studentNames;
    private final ObservableList<Lesson> lessonList = FXCollections.observableArrayList();

    public void initialize() {
        deleteButton.setVisible(false);
        lessonsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            deleteButton.setVisible(newVal != null);
            deleteButton.setManaged(newVal != null);
        });
        lessonsTable.setPlaceholder(new Label("Wybierz dzień, aby wyświelić lekcję."));
        BackgroundWorker.submit( () -> {
            getAllStudentNames();
            Platform.runLater(() -> setTableData());
        });

        EventBus.subscribe(LessonsForDateFoundEvent.class, event ->
                Platform.runLater(() -> refreshTable(event))
        );

        EventBus.subscribe(newDateSelectedEvent.class, event -> {
            if(studentsListSet) {
                selectedDate = event.getDate();
                setDateLabel(selectedDate);
                getLessonForSelectedDay(event);
            }
        });
    }

    private void setDateLabel(LocalDate date) {
        if (Objects.equals(selectedDate, LocalDate.now())) {
            selectedDateLabel.getStyleClass().add("today-text");
        }
        else {
            selectedDateLabel.getStyleClass().remove("today-text");
        }
        selectedDateLabel.setText("Lekcje dnia: " + date.format(DateTimeFormatter.ofPattern("dd.MM")));
    }

    private void getLessonForSelectedDay(newDateSelectedEvent event) {
        BackgroundWorker.submit( () -> {
            List<Lesson> fromDb = LessonDAO.findByTutorAndDate(CurrentSession.getTutorId(),event.getDate());
            fromDb.sort(Comparator.comparing(Lesson::getStartTime));
            EventBus.publish(new LessonsForDateFoundEvent(fromDb));
        });
    }

    private void refreshTable(LessonsForDateFoundEvent event){
        if(event.getLessons().isEmpty()){
            lessonsTable.setPlaceholder(new Label("Brak lekcji na dany dzień."));
        }
        lessonList.setAll(event.getLessons());
        lessonsTable.refresh();
    }

    public void setTableData() {
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"))));
        studentColumn.setCellValueFactory(cellData -> {
            int studentId = cellData.getValue().getStudentId();
            String name = studentNames.getOrDefault(studentId, "ID: " + studentId);
            return new SimpleStringProperty(name);
        });
        subjectColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubject()));
        statusColumn.setCellValueFactory(cellData -> {
            Lesson lesson = cellData.getValue();
            Boolean canceled = lesson.isCanceled();
            Boolean paid = lesson.isPaid();
            String status;

            if (Boolean.TRUE.equals(canceled)) {
                status = "Anulowana";
            } else if (Boolean.TRUE.equals(paid)) {
                status = "Zakończona";
            } else {
                // Compute if lesson has already ended
                LocalDateTime endTime = lesson.getDate()
                        .atTime(lesson.getStartTime())
                        .plusMinutes(lesson.getDurationMinutes());

                if (endTime.isBefore(LocalDateTime.now())) {
                    status = "Nieopłacona";
                } else {
                    status = "Planowa";
                }
            }

            return new SimpleStringProperty(status);
        });

        lessonsTable.setItems(lessonList);
        studentsListSet = true;
    }

    public void getAllStudentNames() {
        List<TutorStudent> fromDb = TutorStudentDAO.findByTutorId(CurrentSession.getTutorId());
        Map<Integer, String> map = new HashMap<>();
        for (TutorStudent ts : fromDb ) {
            map.put(ts.getStudentId(), ts.getDefault_name());
        }
        setStudentNames(map);
    }
    public void setStudentNames(Map<Integer, String> studentNames) {
        this.studentNames = studentNames;
    }

    public void handleNewLesson(ActionEvent event) {
        SceneSwitcherService.loadMainPanel("/newLesson.fxml", (NewLessonController c) ->{c.setSelectedDate(selectedDate);});
    }
    @FXML
    public void handleDeleteLesson(ActionEvent event) {
        Lesson selected = lessonsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            MessagePopupManager.sendPopup("Nie wybrano lekcji do usunięcia.", InfoMessageController.MessageType.ERROR);
            return;
        }

        BackgroundWorker.submit(() -> {
            boolean deleted = LessonDAO.deleteById(selected.getId());
            if (deleted) {
                Platform.runLater(() -> {
                    lessonList.remove(selected);
                    MessagePopupManager.sendPopup("Lekcja została usunięta.", InfoMessageController.MessageType.SUCCESS);
                });
            } else {
                Platform.runLater(() ->
                        MessagePopupManager.sendPopup("Nie udało się usunąć lekcji.", InfoMessageController.MessageType.ERROR)
                );
            }
        });
    }

}
