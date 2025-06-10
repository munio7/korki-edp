package org.example.korkiedp.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import org.example.korkiedp.app.MessagePopupManager;
import org.example.korkiedp.component.InfoMessageController;
import org.example.korkiedp.events.EventBus;
import org.example.korkiedp.events.ShowMessageEvent;
import org.example.korkiedp.service.TutorAuthService;
import org.example.korkiedp.service.SceneSwitcherService;
import org.example.korkiedp.model.Tutor;
import org.example.korkiedp.util.Database;

import java.sql.Connection;
import java.sql.SQLException;

public class LoginController {

    public CheckBox rememberMeCheckBox;
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        Tutor tutor = TutorAuthService.login(username, password, rememberMeCheckBox.isSelected());

        if (tutor != null) {
            MessagePopupManager.sendPopup("Pomyślnie zalogowano", InfoMessageController.MessageType.SUCCESS);
            SceneSwitcherService.loadContentLayer("/main_panel.fxml");
        } else {
            errorLabel.setText("Nieprawidłowy login lub hasło.");
        }
    }

    @FXML
    private void handleBackToWelcome(ActionEvent event) {
        SceneSwitcherService.loadContentLayer("/welcome.fxml");
    }
}
