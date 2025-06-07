package org.example.korkiedp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;
import org.example.korkiedp.model.Tutor;
import org.example.korkiedp.service.SceneSwitcherService;
import org.example.korkiedp.service.TutorAuthService;
import org.example.korkiedp.session.CurrentSession;

import static org.example.korkiedp.service.SceneSwitcherService.loadView;
import static org.example.korkiedp.service.SceneSwitcherService.switchScene;

public class MainPanelController {

    @FXML private StackPane mainContent;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Tutor loggedTutor;

    public void initialize() {
        loggedTutor = CurrentSession.getTutor();
        welcomeLabel.setText("Witaj, " + loggedTutor.getFullName() + "!");
    }

    @FXML
    public void showStudentsView() {
        loadView("/students.fxml", mainContent);
    }

    @FXML
    public void showCalendarView() {
        loadView("/calendar.fxml",mainContent);
    }

    @FXML
    public void showPaymentsView() {
        loadView("/payments.fxml",mainContent);
    }

    @FXML
    public void showSettingsView() {
        loadView("/settings_view.fxml",mainContent);
    }

    @FXML
    public void logout(ActionEvent event) {
        TutorAuthService.logout();
        SceneSwitcherService.switchScene("/welcome.fxml");

    }


}
