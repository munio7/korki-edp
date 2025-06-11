package org.example.korkiedp.events;

import org.example.korkiedp.model.Lesson;

public class LessonSelectedEvent {
    Lesson lesson;

    public LessonSelectedEvent(Lesson lesson) {
        this.lesson = lesson;
    }
    public Lesson getLesson() {return lesson;}
}
