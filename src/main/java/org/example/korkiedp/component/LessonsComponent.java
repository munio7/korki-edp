package org.example.korkiedp.component;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.korkiedp.model.Lesson;

public class LessonsComponent {
    @FXML
    private TableView<Lesson> lessonsTable;
    @FXML private TableColumn<Lesson, String> timeColumn;
    @FXML private TableColumn<Lesson, String> studentColumn;
    @FXML private TableColumn<Lesson, String> topicColumn;
    @FXML private TableColumn<Lesson, String> statusColumn;

    public void initialize() {
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartTime().toString()));
        studentColumn.setCellValueFactory(cellData -> new SimpleStringProperty("ID: " + cellData.getValue().getStudentId())); // Replace with actual name if available
        topicColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTopic()));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().isCanceled() ? "Anulowana" :
                        (cellData.getValue().getAttendance() != null && cellData.getValue().getAttendance()) ? "Obecna" : "Zaplanowana"
        ));
    }

}
