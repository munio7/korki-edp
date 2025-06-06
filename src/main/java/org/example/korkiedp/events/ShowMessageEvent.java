package org.example.korkiedp.events;

import org.example.korkiedp.component.InfoMessageController;

import java.time.Duration;

public class ShowMessageEvent {
    private final String message;
    private final InfoMessageController.MessageType type;
    private final Duration duration;

    public ShowMessageEvent(String message, InfoMessageController.MessageType type, Duration duration) {
        this.message = message;
        this.type = type;
        this.duration = duration;
    }

    public String getMessage() {
        return message;
    }

    public InfoMessageController.MessageType getType() {
        return type;
    }

    public Duration getDuration() {
        return duration;
    }
}