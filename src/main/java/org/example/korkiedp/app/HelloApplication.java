package org.example.korkiedp.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.korkiedp.events.EventBus;
import org.example.korkiedp.events.ShowMessageEvent;
import org.example.korkiedp.session.MainStageHolder;
import org.example.korkiedp.session.MessagePopupManager;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // zapisuje globalnie stage
        MainStageHolder.set(stage);

        // subskrybuje globalnie do popups
        EventBus.subscribe(ShowMessageEvent.class, event -> {
            MessagePopupManager.show(MainStageHolder.get(), event.getMessage(), event.getType(), event.getDuration());
        });

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/welcome.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
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