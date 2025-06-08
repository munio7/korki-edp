package org.example.korkiedp.component;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.korkiedp.async.DbWorker;
import org.example.korkiedp.dao.TutorStudentDAO;
import org.example.korkiedp.events.EventBus;
import org.example.korkiedp.events.allTutorsStudentsFoundEvent;
import org.example.korkiedp.model.Lesson;
import org.example.korkiedp.model.TutorStudent;
import org.example.korkiedp.session.CurrentSession;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LessonsComponentController {
    @FXML
    private TableView<Lesson> lessonsTable;
    @FXML private TableColumn<Lesson, String> timeColumn;
    @FXML private TableColumn<Lesson, String> studentColumn;
    @FXML private TableColumn<Lesson, String> topicColumn;
    @FXML private TableColumn<Lesson, String> statusColumn;
    private Map<Integer, String> studentNames;

    public void initialize() {
        EventBus.subscribe(allTutorsStudentsFoundEvent.class, event -> setTableData());
        DbWorker.submit( () -> {
            getAllStudentNames();
            EventBus.publish(new allTutorsStudentsFoundEvent(null));
        });

    }

    public void setTableData() {
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartTime().format(DateTimeFormatter.ofPattern("HH-mm"))));
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

}
