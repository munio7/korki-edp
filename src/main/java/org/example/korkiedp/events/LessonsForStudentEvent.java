package org.example.korkiedp.events;

import org.example.korkiedp.model.Lesson;
import org.example.korkiedp.model.Tutor;
import org.example.korkiedp.model.TutorStudent;

import java.util.List;

public class LessonsForStudentEvent {
    TutorStudent student;

    public LessonsForStudentEvent(TutorStudent student) {
        this.student = student;
    }
    public TutorStudent getStudent() {return student;}

}
