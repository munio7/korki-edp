package org.example.korkiedp.events;

public class newLessonEvent {
    boolean status;

    public newLessonEvent(boolean status) {
        this.status = status;
    }
    public boolean getStatus() {return status;}
}
