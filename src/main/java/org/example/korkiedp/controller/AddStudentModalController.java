package org.example.korkiedp.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.example.korkiedp.app.MessagePopupManager;
import org.example.korkiedp.async.DbWorker;
import org.example.korkiedp.component.InfoMessageController;
import org.example.korkiedp.dao.StudentDAO;
import org.example.korkiedp.events.*;
import org.example.korkiedp.model.Student;
import org.example.korkiedp.model.TutorStudent;
import org.example.korkiedp.service.SceneSwitcherService;
import org.example.korkiedp.session.CurrentSession;

import java.util.List;
import java.util.SequencedSet;

public class AddStudentModalController {
    public TableColumn<Student,String> firstNameCol;
    public TableColumn<Student,String> lastNameCol;
    public TableColumn<Student,String> locationCol;
    public TableColumn<Student,String> phoneCol;
    public TextField studentFirstNameField;
    public TextField studentLastNameField;
    public TextField studentLocalization;
    public TextField studentTelNumber;
    public VBox addNewStudentModal;
    public Button showModalBtn;

    @FXML
    private TableView<Student> studentsTable;
    private final ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        studentsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && studentsTable.getSelectionModel().getSelectedItem() != null) {
                Student selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
                System.out.println(selectedStudent.getFirstName() + " " + selectedStudent.getLastName() + " " + selectedStudent.getTelNumber() + selectedStudent.getId());
                EventBus.publish(new newRelationEvent(selectedStudent));
            }
        });

        EventBus.subscribe(allStudentsFoundEvent.class, event -> allStudentsFound(event));
        EventBus.subscribe(newStudentAddedEvent.class, event -> handleStudentAdded(event));

        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("localization"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("telNumber"));


        DbWorker.submit(() -> {
            List<Student> fromDb = StudentDAO.findAllNotInRelationWithTutorId(CurrentSession.getTutorId());
            EventBus.publish(new allStudentsFoundEvent(fromDb));
        });
    }

    public void allStudentsFound(allStudentsFoundEvent event){
        studentList.setAll(event.getFromDb());
        studentsTable.setItems(studentList);
    }
    @FXML
    public void handleAddStudent(ActionEvent event) {
        DbWorker.submit(() -> {
            Student student = new Student(studentFirstNameField.getText().trim(),studentLastNameField.getText().trim(),studentLocalization.getText().trim(),studentTelNumber.getText().trim());
            boolean status = StudentDAO.save(student);
            EventBus.publish(new newStudentAddedEvent(student,status));
        });
    }


    public void handleGoBack(ActionEvent event) {
        SceneSwitcherService.loadMainPanel("/students.fxml");
    }

    public void handleShowModal(ActionEvent event) {
        addNewStudentModal.setVisible(!addNewStudentModal.isVisible());
    }

    private void handleStudentAdded(newStudentAddedEvent event){
        if(event.getStatus()) {
            MessagePopupManager.sendPopup("Pomyślnie dodano studenta: " + event.getStudent() + " do bazy!", InfoMessageController.MessageType.SUCCESS);
            studentList.add(event.getStudent());
            studentsTable.refresh();
            studentFirstNameField.setText("");
            studentLastNameField.setText("");
            studentLocalization.setText("");
            studentTelNumber.setText("");

        }else
            MessagePopupManager.sendPopup("Dodanie studenta:" + event.getStudent() + " do bazy nie powiodło się.", InfoMessageController.MessageType.ERROR);
    }

}
