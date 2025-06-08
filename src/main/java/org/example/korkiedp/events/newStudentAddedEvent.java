package org.example.korkiedp.events;

import org.example.korkiedp.model.Student;

public class newStudentAddedEvent {
    Student student;
    boolean status;

    public newStudentAddedEvent(Student student,boolean status) {
        this.student = student;
        this.status = status;
    }

    public Student getStudent() {
        return student;
    }
    public boolean getStatus() {
        return status;
    }
}
