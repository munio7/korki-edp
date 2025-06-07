package org.example.korkiedp.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.korkiedp.async.DbWorker;
import org.example.korkiedp.dao.StudentDAO;
import org.example.korkiedp.events.EventBus;
import org.example.korkiedp.events.allStudentsFoundEvent;
import org.example.korkiedp.model.Student;
import org.example.korkiedp.service.SceneSwitcherService;
import org.example.korkiedp.session.CurrentSession;

import java.util.List;
import java.util.SequencedSet;

public class AddStudentModalController {
    public TableColumn<Student,String> firstNameCol;
    public TableColumn<Student,String> lastNameCol;
    public TableColumn<Student,String> locationCol;
    public TableColumn<Student,String> phoneCol;

    @FXML
    private TableView<Student> studentsTable;
    private final ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        EventBus.subscribe(allStudentsFoundEvent.class, event -> allStudentsFound(event));

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

    public void handleAddStudent(ActionEvent event) {
    }

    public void handleCancel(ActionEvent event) {

    }

    public void handleGoBack(ActionEvent event) {
        SceneSwitcherService.loadMainPanel("/students.fxml");
    }
}
