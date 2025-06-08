package org.example.korkiedp.component;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.korkiedp.async.DbWorker;
import org.example.korkiedp.dao.TutorStudentDAO;
import org.example.korkiedp.events.EditStudentEvent;
import org.example.korkiedp.events.EventBus;
import org.example.korkiedp.events.allTutorsStudentsFoundEvent;
import org.example.korkiedp.model.TutorStudent;
import org.example.korkiedp.session.CurrentSession;

import java.util.List;

public class StudentsTableController {

    @FXML
    private TableView<TutorStudent> studentsTable;
    @FXML
    private TableColumn<TutorStudent, String> nameColumn;
    @FXML
    private TableColumn<TutorStudent, String> classColumn;
    @FXML
    public TableColumn<TutorStudent, String> priceColumn;
    @FXML
    public TableColumn<TutorStudent, String> activeColumn;

    private final ObservableList<TutorStudent> tutorStudentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        EventBus.subscribe(allTutorsStudentsFoundEvent.class, (event) -> allTutorsStudentsFound(event));

        studentsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && studentsTable.getSelectionModel().getSelectedItem() != null) {
                TutorStudent selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
                EventBus.publish(new EditStudentEvent(selectedStudent));
            }
        });

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("default_name"));
        classColumn.setCellValueFactory(cellData -> {
            String level = cellData.getValue().getclassName();
            return new SimpleStringProperty((level == "" || level == null) ? "Brak" : level);
        });
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("default_price"));
        activeColumn.setCellValueFactory(cellData -> {
            boolean isActive = cellData.getValue().getActive();
            return new SimpleStringProperty(isActive ? "Aktywna" : "Nieaktywna");
        });


        DbWorker.submit(()->{
            List<TutorStudent> fromDb = TutorStudentDAO.findByTutorId(CurrentSession.getTutorId());
            EventBus.publish(new allTutorsStudentsFoundEvent(fromDb));
        });


    }

    private void allTutorsStudentsFound(allTutorsStudentsFoundEvent event ) {
        studentsTable.setItems(tutorStudentList);
        tutorStudentList.setAll(event.getFromDb());
    }
}
