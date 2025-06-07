package org.example.korkiedp.component;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.korkiedp.dao.TutorStudentDAO;
import org.example.korkiedp.events.EditStudentEvent;
import org.example.korkiedp.events.EventBus;
import org.example.korkiedp.model.Student;
import org.example.korkiedp.model.TutorStudent;
import org.example.korkiedp.session.CurrentSession;

import java.awt.desktop.SystemEventListener;
import java.util.List;

public class StudentsTableController {

    @FXML
    private TableView<TutorStudent> studentsTable;
    @FXML
    private TableColumn<TutorStudent, String> nameColumn;
    @FXML
    private TableColumn<TutorStudent, String> levelColumn;
    @FXML
    public TableColumn<TutorStudent, String> priceColumn;
    @FXML
    public TableColumn<TutorStudent, String> activeColumn;

    private final ObservableList<TutorStudent> tutorStudentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        studentsTable.setOnMouseClicked(event -> {
            if (studentsTable.getSelectionModel().getSelectedItem() != null) {
                System.out.println("Wszedłem");
                TutorStudent selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
                EventBus.publish(new EditStudentEvent(selectedStudent));
            }
        });

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("default_name"));
        levelColumn.setCellValueFactory(cellData -> {
            String level = cellData.getValue().getLevel();
            return new SimpleStringProperty((level == "" || level == null) ? "Brak" : level);
        });
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("default_price"));
        activeColumn.setCellValueFactory(cellData -> {
            boolean isActive = cellData.getValue().getActive();
            return new SimpleStringProperty(isActive ? "Aktywna" : "Nieaktywna");
        });

        studentsTable.setItems(tutorStudentList);

        List<TutorStudent> fromDb = TutorStudentDAO.findByTutorId(CurrentSession.getTutorId());
        tutorStudentList.setAll(fromDb);

    }
}
