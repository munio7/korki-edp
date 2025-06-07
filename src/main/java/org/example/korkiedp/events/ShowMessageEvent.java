package org.example.korkiedp.events;
import org.example.korkiedp.component.InfoMessageController;


public class ShowMessageEvent {
    private final String message;
    private final InfoMessageController.MessageType type;

    public ShowMessageEvent(String message, InfoMessageController.MessageType type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public InfoMessageController.MessageType getType() {
        return type;
    }

}