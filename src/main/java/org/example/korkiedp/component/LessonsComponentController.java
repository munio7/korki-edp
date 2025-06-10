package org.example.korkiedp.component;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.korkiedp.app.MessagePopupManager;
import org.example.korkiedp.async.DbWorker;
import org.example.korkiedp.controller.NewLessonController;
import org.example.korkiedp.dao.LessonDAO;
import org.example.korkiedp.dao.TutorStudentDAO;
import org.example.korkiedp.events.EventBus;
import org.example.korkiedp.events.LessonsForDateFoundEvent;
import org.example.korkiedp.events.allTutorsStudentsFoundEvent;
import org.example.korkiedp.events.newDateSelectedEvent;
import org.example.korkiedp.model.Lesson;
import org.example.korkiedp.model.TutorStudent;
import org.example.korkiedp.service.SceneSwitcherService;
import org.example.korkiedp.session.CurrentSession;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LessonsComponentController {
    public Label selectedDateLabel;
    @FXML
    private TableView<Lesson> lessonsTable;
    @FXML private TableColumn<Lesson, String> timeColumn;
    @FXML private TableColumn<Lesson, String> studentColumn;
    @FXML private TableColumn<Lesson, String> topicColumn;
    @FXML private TableColumn<Lesson, String> statusColumn;
    private LocalDate selectedDate;

    private Map<Integer, String> studentNames;
    private final ObservableList<Lesson> lessonList = FXCollections.observableArrayList();

    public void initialize() {
        selectedDate = LocalDate.now();
        setDateLabel(selectedDate);
        getLessonForSelectedDay(selectedDate);

        EventBus.subscribe(allTutorsStudentsFoundEvent.class, event -> setTableData());
        EventBus.subscribe(newDateSelectedEvent.class, event -> {
            selectedDate = event.getDate();
            setDateLabel(selectedDate);
            getLessonForSelectedDay(event);
        });
        EventBus.subscribe(LessonsForDateFoundEvent.class, event ->
                Platform.runLater(() -> refreshTable(event))
        );
        DbWorker.submit( () -> {
            getAllStudentNames();
            EventBus.publish(new allTutorsStudentsFoundEvent(null));
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

    private void getLessonForSelectedDay(LocalDate date) {
        DbWorker.submit( () -> {
            List<Lesson> fromDb = LessonDAO.findByTutorAndDate(CurrentSession.getTutorId(),date);
            EventBus.publish(new LessonsForDateFoundEvent(fromDb));
        });
    }

    private void getLessonForSelectedDay(newDateSelectedEvent event) {
        DbWorker.submit( () -> {
            List<Lesson> fromDb = LessonDAO.findByTutorAndDate(CurrentSession.getTutorId(),event.getDate());
            EventBus.publish(new LessonsForDateFoundEvent(fromDb));
        });
    }

    private void refreshTable(LessonsForDateFoundEvent event){
        if(event.getLessons().isEmpty()){
            MessagePopupManager.sendPopup("Brak lekcji na dany dzień", InfoMessageController.MessageType.ERROR);
        }
        lessonList.setAll(event.getLessons());
    }

    public void setTableData() {
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"))));
        studentColumn.setCellValueFactory(cellData -> {
            int studentId = cellData.getValue().getStudentId();
            String name = studentNames.getOrDefault(studentId, "ID: " + studentId);
            return new SimpleStringProperty(name);
        });
        topicColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubject()));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().isCanceled() ? "Anulowana" :
                        (cellData.getValue().getAttendance() != null && cellData.getValue().getAttendance()) ? "Obecna" : "Zaplanowana"
        ));

        lessonsTable.setItems(lessonList);
    }

    public void getAllStudentNames() {
        Map<Integer, String> map = new HashMap<>();
        for (TutorStudent ts : TutorStudentDAO.findByTutorId(CurrentSession.getTutorId())) {
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
}
