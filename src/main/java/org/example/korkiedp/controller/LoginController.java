package org.example.korkiedp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
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

    private final TutorAuthService authService = new TutorAuthService();

    @FXML
    private void handleLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        Tutor tutor = TutorAuthService.login(username, password, rememberMeCheckBox.isSelected());

        if (tutor != null) {
            SceneSwitcherService.switchScene(event, "/main_panel.fxml", "Panel główny");
        } else {
            errorLabel.setText("Nieprawidłowy login lub hasło.");
        }
    }

    @FXML
    private void handleBackToWelcome(ActionEvent event) {
        SceneSwitcherService.switchScene(event, "/welcome.fxml", "Welcome");
    }
}
