package org.example.korkiedp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
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
    private Button studentsViewBtn;
    @FXML
    private Button calendarViewBtn;
    @FXML
    private Button paymentsViewBtn;
    @FXML
    private Button settingsViewBtn;

    private Button selectedButton;

    private void highlightSelected(Button selected) {
        if (selectedButton != null) {
            selectedButton.getStyleClass().remove("active");
        }
        selected.getStyleClass().add("active");
        selectedButton = selected;
    }

    private Tutor loggedTutor;

    public void initialize() {
        MainStageHolder.setMainPanelPane(mainContent);
        loggedTutor = CurrentSession.getTutor();
        welcomeLabel.setText("Witaj, " + loggedTutor.getFullName() + "!");
    }

    @FXML
    public void showStudentsView() {
        SceneSwitcherService.loadMainPanel("/students.fxml");
        highlightSelected(studentsViewBtn);
    }

    @FXML
    public void showCalendarView() {
        SceneSwitcherService.loadMainPanel("/calendar.fxml");
        highlightSelected(calendarViewBtn);
    }

    @FXML
    public void showPaymentsView() {
        SceneSwitcherService.loadMainPanel("/payments.fxml");
        highlightSelected(paymentsViewBtn);
    }

    @FXML
    public void showSettingsView() {
        SceneSwitcherService.loadMainPanel("/settings_view.fxml");
        highlightSelected(settingsViewBtn);
    }


    @FXML
    public void logout(ActionEvent event) {
        TutorAuthService.logout();
        SceneSwitcherService.loadContentLayer("/welcome.fxml");
    }

}
