package org.example.korkiedp.session;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.korkiedp.component.InfoMessageController;

public class MessagePopupManager {

    public static void show(Stage ownerStage, String message, InfoMessageController.MessageType type, Duration duration) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(MessagePopupManager.class.getResource("/InfoMessage.fxml"));
                Parent root = loader.load();
                InfoMessageController controller = loader.getController();
                controller.setMessage(message, type);

                Popup popup = new Popup();
                popup.getContent().add(root);
                popup.setAutoHide(true);
                popup.show(ownerStage);

                PauseTransition delay = new PauseTransition(duration);
                delay.setOnFinished(e -> popup.hide());
                delay.play();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
