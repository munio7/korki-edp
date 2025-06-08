package org.example.korkiedp.events;

import org.example.korkiedp.model.TutorStudent;

public class newRelationSetEvent {
    TutorStudent ts;
    Boolean status;

    public newRelationSetEvent(TutorStudent ts, boolean status) {this.ts = ts; this.status = status;}
    public TutorStudent getTutorStudent() {return ts;}
    public Boolean getStatus() {return status;}
}
