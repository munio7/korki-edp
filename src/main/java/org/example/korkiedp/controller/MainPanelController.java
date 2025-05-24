package org.example.korkiedp.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import org.example.korkiedp.model.Tutor;
import org.example.korkiedp.service.SceneSwitcherService;
import org.example.korkiedp.service.TutorAuthService;
import org.example.korkiedp.session.CurrentSession;

public class MainPanelController {

    @FXML
    private Label welcomeLabel;

    private Tutor loggedTutor;

    public void initialize() {
        loggedTutor = CurrentSession.getTutor();
        welcomeLabel.setText("Witaj, " + loggedTutor.getFullName() + "!");
    }

    @FXML
    private void handleStudents(ActionEvent event) {
        SceneSwitcherService.switchScene(event,"/students.fxml");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        TutorAuthService.logout();
        SceneSwitcherService.switchScene(event, "/welcome.fxml", "Powitanie");
    }

    @FXML
    public void handleCalendar(ActionEvent event) {
        SceneSwitcherService.switchScene(event,"/calendar.fxml");
    }
}
