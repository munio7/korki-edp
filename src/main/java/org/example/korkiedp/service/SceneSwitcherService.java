package org.example.korkiedp.service;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jdk.jfr.Event;
import org.example.korkiedp.session.MainStageHolder;


import java.io.IOException;
import java.util.Objects;

public class SceneSwitcherService {

    public static void switchScene(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneSwitcherService.class.getResource(fxmlPath)));
            Scene currentScene = MainStageHolder.get().getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
            // You can log or show error dialog here
        }
    }

    public static void switchScene(String fxmlPath) {
        switchScene(fxmlPath, null);
    }

    public static void loadView(String fxmlPath, Pane container) {
        try {
            Parent view = FXMLLoader.load(Objects.requireNonNull(SceneSwitcherService.class.getResource(fxmlPath)));
            container.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
            // Log or show alert
        }
    }
}
