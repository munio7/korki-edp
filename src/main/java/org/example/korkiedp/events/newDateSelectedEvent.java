package org.example.korkiedp.events;

import java.time.LocalDate;

public class newDateSelectedEvent {
    LocalDate date;

    public newDateSelectedEvent(LocalDate date) {
        this.date = date;
    }
    public LocalDate getDate() {return date;}
}
