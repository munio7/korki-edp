package org.example.korkiedp.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.korkiedp.events.EditStudentEvent;
import org.example.korkiedp.events.EventBus;
import org.example.korkiedp.events.ShowMessageEvent;
import org.example.korkiedp.service.SceneSwitcherService;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Subscribe popup listener globally
        EventBus.subscribe(ShowMessageEvent.class, event -> {
            Platform.runLater(() -> {
                MessagePopupManager.show(event.getMessage(), event.getType());
            });
        });


        // Load base layout
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/appRoot.fxml"));
        StackPane root = loader.load();

        // Grab references to layers
        AnchorPane contentLayer = (AnchorPane) root.lookup("#contentLayer");
        AnchorPane popupLayer = (AnchorPane) root.lookup("#popupLayer");

        // Register them globally
        MainStageHolder.set(stage, popupLayer, contentLayer);

        // Load initial view
        SceneSwitcherService.loadContentLayer("/welcome.fxml");

        // Set up stage
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setTitle("Korki Manager");
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}