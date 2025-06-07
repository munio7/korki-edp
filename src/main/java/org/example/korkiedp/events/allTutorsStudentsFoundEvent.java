package org.example.korkiedp.events;

import org.example.korkiedp.model.Student;
import org.example.korkiedp.model.Tutor;
import org.example.korkiedp.model.TutorStudent;

import java.util.List;

public class allTutorsStudentsFoundEvent {
    List<TutorStudent> fromDb;
    public allTutorsStudentsFoundEvent(List<TutorStudent> students) {this.fromDb = students;}
    public List<TutorStudent> getFromDb() {return fromDb;}
}

