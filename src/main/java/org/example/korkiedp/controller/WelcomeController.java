package org.example.korkiedp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.korkiedp.component.InfoMessageController;
import org.example.korkiedp.events.EventBus;
import org.example.korkiedp.events.ShowMessageEvent;
import org.example.korkiedp.service.SceneSwitcherService;
import org.example.korkiedp.service.TutorAuthService;
import org.example.korkiedp.util.ConfigProvider;

public class WelcomeController {

    public Button loginButton;
    public Button registerButton;

    public void handleLogin() {
        if (ConfigProvider.isSet("remember.login") && ConfigProvider.isSet("remember.password")) {
            TutorAuthService.login((String) ConfigProvider.get("remember.login"),(String) ConfigProvider.get("remember.password"),false);
            loadScene("/main_panel.fxml");
        }
        else
            loadScene("/login.fxml");
    }

    public void handleRegister() {
        loadScene("/signup.fxml");
    }

    private void loadScene(String fxmlPath) {
        SceneSwitcherService.loadContentLayer(fxmlPath);
    }
}
