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
import org.example.korkiedp.session.MainStageHolder;

public class MainPanelController {

    @FXML private StackPane mainContent;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Tutor loggedTutor;

    public void initialize() {
        MainStageHolder.setMainPanelPane(mainContent);
        loggedTutor = CurrentSession.getTutor();
        welcomeLabel.setText("Witaj, " + loggedTutor.getFullName() + "!");
    }

    @FXML
    public void showStudentsView() {
        SceneSwitcherService.loadMainPanel("/students.fxml");
    }

    @FXML
    public void showCalendarView() {
        SceneSwitcherService.loadMainPanel("/calendar.fxml");
    }

    @FXML
    public void showPaymentsView() {
        SceneSwitcherService.loadMainPanel("/payments.fxml");
    }

    @FXML
    public void showSettingsView() {
        SceneSwitcherService.loadMainPanel("/settings_view.fxml");
    }

    @FXML
    public void logout(ActionEvent event) {
        TutorAuthService.logout();
        SceneSwitcherService.loadContentLayer("/welcome.fxml");

    }


}
