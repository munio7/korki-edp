package org.example.korkiedp.events;

import org.example.korkiedp.model.Student;
import org.example.korkiedp.model.TutorStudent;

public class StudentEditedEvent {
    private TutorStudent student;
    private boolean status;

    public StudentEditedEvent(TutorStudent student,boolean status) {this.student = student; this.status = status;}

    public TutorStudent getStudent() {return student;}
    public boolean getStatus() {return status;}
}
