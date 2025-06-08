package org.example.korkiedp.events;

import org.example.korkiedp.model.Student;

public class newRelationEvent {
    Student student;

    public newRelationEvent(Student student) {this.student = student;}
    public Student getStudent() {return student;}
}
