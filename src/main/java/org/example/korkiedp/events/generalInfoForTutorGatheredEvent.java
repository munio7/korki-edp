package org.example.korkiedp.events;

import java.math.BigDecimal;

public class generalInfoForTutorGatheredEvent {
    int studentsActive;
    int lessonsCompleted;
    BigDecimal totalEarnings;

    public generalInfoForTutorGatheredEvent(int studentsActive, int lessonsCompleted, BigDecimal totalEarnings) {
        this.studentsActive = studentsActive;
        this.lessonsCompleted = lessonsCompleted;
        this.totalEarnings = totalEarnings;
    }

    public int getStudentsActive() {return studentsActive;}
    public int getLessonsCompleted() {return lessonsCompleted;}
    public BigDecimal getTotalEarnings() {return totalEarnings;}
}
