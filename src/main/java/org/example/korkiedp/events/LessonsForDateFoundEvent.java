package org.example.korkiedp.events;

import org.example.korkiedp.model.Lesson;

import java.util.List;

public class LessonsForDateFoundEvent {
    List<Lesson> lessons;
    public LessonsForDateFoundEvent(List<Lesson> lessons) {
        this.lessons = lessons;
    }
    public List<Lesson> getLessons() {return lessons;}
}
