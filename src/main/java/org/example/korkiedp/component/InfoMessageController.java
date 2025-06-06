package org.example.korkiedp.component;

import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class InfoMessageController {

    @FXML
    private Text messageText;

    public void setMessage(String message, MessageType type) {
        messageText.setText(message);
        messageText.setFill(type == MessageType.SUCCESS ? Color.GREEN : Color.RED);
    }

    public enum MessageType {
        SUCCESS, ERROR
    }
}