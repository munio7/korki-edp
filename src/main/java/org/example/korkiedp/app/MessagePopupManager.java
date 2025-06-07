package org.example.korkiedp.app;

import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.korkiedp.component.InfoMessageController;

public class MessagePopupManager {

    public static void show(String message, InfoMessageController.MessageType type) {
        try {
            System.out.println("Showing message: " + message);
            FXMLLoader loader = new FXMLLoader(MessagePopupManager.class.getResource("/InfoMessage.fxml"));
            AnchorPane messagePane = loader.load();

            InfoMessageController controller = loader.getController();
            controller.setMessage(message, type);

            // Center horizontally, top = 50px
            Stage stage = MainStageHolder.get();
            double x = (stage.getWidth() - messagePane.getPrefWidth()) / 2;
            messagePane.setLayoutX(x);
            messagePane.setLayoutY(50);

            AnchorPane popupLayer = MainStageHolder.getPopupLayer();
            if (popupLayer != null) {
                popupLayer.getChildren().add(messagePane);
            }

            // Auto-dismiss
            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(e -> popupLayer.getChildren().remove(messagePane));
            delay.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
