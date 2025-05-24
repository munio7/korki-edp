package org.example.korkiedp.service;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jdk.jfr.Event;


import java.io.IOException;
import java.util.Objects;

public class SceneSwitcherService {

    public static void switchScene(ActionEvent sourceEvent, String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneSwitcherService.class.getResource(fxmlPath)));
            Scene currentScene = (Scene) ((Node) sourceEvent.getSource()).getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
            // You can log or show error dialog here
        }
    }

    /**
     * Overload without title.
     */
    public static void switchScene(ActionEvent sourceEvent, String fxmlPath) {
        switchScene(sourceEvent, fxmlPath, null);
    }
}
