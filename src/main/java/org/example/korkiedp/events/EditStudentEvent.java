package org.example.korkiedp.events;

import org.example.korkiedp.model.TutorStudent;

public class EditStudentEvent {
    private final TutorStudent tutorStudent;

    public EditStudentEvent(TutorStudent tutorStudent) {
        this.tutorStudent = tutorStudent;
    }

    public TutorStudent getTutorStudent() {
        return tutorStudent;
    }
}