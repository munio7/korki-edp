package org.example.korkiedp.component;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.example.korkiedp.app.MainStageHolder;

public class InfoMessageController {

    @FXML
    private Text messageText;

    @FXML
    private AnchorPane rootPane;

    public void setMessage(String message, MessageType type) {
        messageText.setText(message);
        messageText.setFill(type == MessageType.SUCCESS ? Color.GREEN : Color.RED);

        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            rootPane.setLayoutX((MainStageHolder.get().getWidth() - newVal.doubleValue()) / 2);
        });
    }

    public enum MessageType {
        SUCCESS, ERROR
    }
}
