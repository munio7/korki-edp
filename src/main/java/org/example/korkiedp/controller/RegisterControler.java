package org.example.korkiedp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.korkiedp.dao.TutorDAO;
import org.example.korkiedp.model.Tutor;
import org.example.korkiedp.service.SceneSwitcherService;
import org.example.korkiedp.service.TutorAuthService;
import org.example.korkiedp.util.PasswordUtil;


public class RegisterControler {
    public Label successLabel;
    public Label errorLabel;
    public PasswordField confirmPasswordField;
    public TextField usernameField;
    public PasswordField passwordField;
    public TextField fullNameField;
    public Button registerButton;
    public TextField emailField;

    @FXML
    private void handleRegisterButtonClick() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String fullName = fullNameField.getText();

        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || !password.trim().equals(confirmPassword.trim())){
            errorLabel.setText("Wypełnij wszystkie pola lub wprowadzono błędne potwierdzenie hasła.");
            return;
        }

        String hashedPassword = PasswordUtil.hash(password);

        Tutor registeredTutor = TutorAuthService.register(new Tutor(username,hashedPassword,fullName,email));

        if (registeredTutor != null){
            errorLabel.setText("");
            successLabel.setText("Dodano nowego korepetytora!");
            eraseFields();

        }
        System.out.println(registeredTutor);
    }

    private void eraseFields(){
        fullNameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        usernameField.clear();
    }

    @FXML
    private void handleBackToWelcome(ActionEvent event) {
        SceneSwitcherService.loadContentLayer("/welcome.fxml");
    }
}
