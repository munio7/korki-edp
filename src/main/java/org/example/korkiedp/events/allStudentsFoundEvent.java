package org.example.korkiedp.events;

import org.example.korkiedp.model.Student;

import java.util.List;

public class allStudentsFoundEvent {
    List<Student> fromDb;
    public allStudentsFoundEvent(List<Student> students) {this.fromDb = students;}
    public List<Student> getFromDb() {return fromDb;}
}
