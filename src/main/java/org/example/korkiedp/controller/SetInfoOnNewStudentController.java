package org.example.korkiedp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import org.example.korkiedp.async.DbWorker;
import org.example.korkiedp.component.InfoMessageController;
import org.example.korkiedp.dao.TutorStudentDAO;
import org.example.korkiedp.events.EventBus;
import org.example.korkiedp.events.StudentEditedEvent;
import org.example.korkiedp.events.newRelationSetEvent;
import org.example.korkiedp.model.Student;
import org.example.korkiedp.model.TutorStudent;
import org.example.korkiedp.service.SceneSwitcherService;
import org.example.korkiedp.session.CurrentSession;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import static org.example.korkiedp.app.MessagePopupManager.sendPopup;

public class SetInfoOnNewStudentController {
    public TextField nameField;
    public TextField classField;
    public TextField priceField;
    public CheckBox activeCheckbox;
    public FlowPane daysPane;
    public TextArea notesArea;
    public Button saveButton;
    public Button cancelButton;

    Student student;

    @FXML
    private void initialize() {
        EventBus.subscribe(newRelationSetEvent.class, event -> handleNewRelation(event));
    }

    public void setData(Student s) {
        this.student = s;
        nameField.setText(s.getFirstName() + " " + s.getLastName());
    }

    @FXML
    private void handleSave() {
        if(nameField.getText().isEmpty()) {
            sendPopup("Nazwij ucznia!", InfoMessageController.MessageType.ERROR);
            return;
        }
        if(classField.getText().isEmpty()) {
            sendPopup("Określ klasę!", InfoMessageController.MessageType.ERROR);
            return;
        }

        TutorStudent tutorStudent = new TutorStudent(CurrentSession.getTutorId(),student.getId());
        tutorStudent.setDefault_name(nameField.getText());
        tutorStudent.setClassName(classField.getText());
        try {
            tutorStudent.setDefault_price(new BigDecimal(priceField.getText()));
        } catch (NumberFormatException e) {
            sendPopup("Zła cena", InfoMessageController.MessageType.ERROR);
            return;
        }

        tutorStudent.setActive(activeCheckbox.isSelected());
        tutorStudent.setNotes(notesArea.getText());

        String selectedDays = daysPane.getChildren().stream()
                .filter(node -> node instanceof CheckBox && ((CheckBox) node).isSelected())
                .map(node -> ((CheckBox) node).getText())
                .collect(Collectors.joining(","));
        tutorStudent.setPreferredDays(selectedDays);

        DbWorker.submit(() ->{
            boolean status = TutorStudentDAO.save(tutorStudent);
            EventBus.publish(new newRelationSetEvent(tutorStudent, status));
        });

    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void handleNewRelation(newRelationSetEvent event) {

        if(event.getStatus()) {
            sendPopup("Zawarto relację z " + event.getTutorStudent().getDefault_name(), InfoMessageController.MessageType.SUCCESS);
            closeWindow();
        }
        else
            sendPopup("Coś poszło nie tak z zawarciem relacji, spróbuj ponownie.", InfoMessageController.MessageType.ERROR);
    }

    private void closeWindow() {
        SceneSwitcherService.loadMainPanel("/newRelationView.fxml");
    }
}
