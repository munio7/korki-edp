package org.example.korkiedp.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TutorStudent {
    private int tutorId;
    private int studentId;
    private LocalDate start_date;
    private boolean active;
    private BigDecimal default_price;
    private String preferred_days;
    private String preferred_hours;
    private String level;
    private String notes;
    private LocalDateTime last_contacted_at;


    public TutorStudent() {}

    public TutorStudent(int tutorId, int studentId) {
        this.tutorId = tutorId;
        this.studentId = studentId;
        this.active = true;
        this.start_date = LocalDate.now();
    }


    public int getTutorId() {
        return tutorId;
    }

    public void setTutorId(int tutorId) {
        this.tutorId = tutorId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public LocalDate getStartDate() {
        return start_date;
    }

    public void setStartDate(LocalDate start_date) {
        this.start_date = start_date;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public BigDecimal getDefaultPrice() {
        return default_price;
    }

    public void setDefaultPrice(BigDecimal default_price) {
        this.default_price = default_price;
    }

    public String getPreferredDays() {
        return preferred_days;
    }

    public void setPreferredDays(String preferred_days) {
        this.preferred_days = preferred_days;
    }

    public String getPreferredHours() {
        return preferred_hours;
    }

    public void setPreferredHours(String preferred_hours) {
        this.preferred_hours = preferred_hours;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
