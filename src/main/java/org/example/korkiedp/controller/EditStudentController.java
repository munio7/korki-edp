package org.example.korkiedp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import org.example.korkiedp.async.DbWorker;
import org.example.korkiedp.component.InfoMessageController;
import org.example.korkiedp.dao.TutorStudentDAO;
import org.example.korkiedp.events.EventBus;
import org.example.korkiedp.events.StudentEditedEvent;
import org.example.korkiedp.model.TutorStudent;
import org.example.korkiedp.service.SceneSwitcherService;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import static org.example.korkiedp.app.MessagePopupManager.sendPopup;

public class EditStudentController {

    @FXML private TextField nameField;
    @FXML private TextField classField;
    @FXML private TextField priceField;
    @FXML private CheckBox activeCheckbox;
    @FXML private FlowPane daysPane;
    @FXML private TextArea notesArea;

    private TutorStudent tutorStudent;

    @FXML
    private void initialize() {
        EventBus.subscribe(StudentEditedEvent.class,event -> handleStudentEdited(event));
    }

    public void setData(TutorStudent ts) {
        this.tutorStudent = ts;
        nameField.setText(ts.getDefault_name());
        classField.setText(ts.getclassName());
        priceField.setText(ts.getDefault_price() != null ? ts.getDefault_price().toString() : "");
        activeCheckbox.setSelected(ts.getActive());
        notesArea.setText(ts.getNotes());

        // Set selected days (assuming CSV format in DB: "Poniedziałek,Środa")
        String[] selectedDays = ts.getPreferredDays() != null ? ts.getPreferredDays().split(",") : new String[0];
        daysPane.getChildren().filtered(node -> node instanceof CheckBox).forEach(node -> {
            CheckBox cb = (CheckBox) node;
            cb.setSelected(java.util.Arrays.asList(selectedDays).contains(cb.getText()));
        });
    }

    @FXML
    private void handleSave() {
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
            boolean status = TutorStudentDAO.updateAll(tutorStudent);
            EventBus.publish(new StudentEditedEvent(tutorStudent, status));
        });

    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void handleStudentEdited(StudentEditedEvent event) {

        if(event.getStatus()) {
            sendPopup("Zapisano zmiany dla ucznia: " + event.getStudent().getDefault_name(), InfoMessageController.MessageType.SUCCESS);
            closeWindow();
        }
        else
            sendPopup("Coś poszło nie tak z edycją danych, spróbuj ponownie.", InfoMessageController.MessageType.ERROR);
    }

    private void closeWindow() {
        SceneSwitcherService.loadMainPanel("/students.fxml");
    }
}
