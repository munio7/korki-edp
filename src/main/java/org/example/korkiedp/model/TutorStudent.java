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
    private String className;
    private String notes;
    private LocalDateTime created_at;
    private LocalDateTime last_contacted_at;
    private String default_name;

    // Empty
    public TutorStudent() {}

    // Full
    public TutorStudent(int tutorId, int studentId, LocalDate start_date, boolean active, BigDecimal default_price, String preferred_days, String preferred_hours, String className, String notes, LocalDateTime last_contacted_at,LocalDateTime created_at,String default_name) {
        this.tutorId = tutorId;
        this.studentId = studentId;
        this.start_date = start_date;
        this.active = active;
        this.default_price = default_price;
        this.preferred_days = preferred_days;
        this.preferred_hours = preferred_hours;
        this.className = className;
        this.notes = notes;
        this.created_at = created_at;
        this.last_contacted_at = last_contacted_at;
        this.default_name = default_name;
    }

    public TutorStudent(int tutorId, int studentId) {
        this.tutorId = tutorId;
        this.studentId = studentId;
        this.active = true;
        this.start_date = LocalDate.now();
        this.default_price = BigDecimal.ZERO;
        this.preferred_days = "";
        this.preferred_hours = "";
        this.className = "";
        this.notes = "";
        this.created_at = LocalDateTime.now();
        this.last_contacted_at = null;


    }
    public TutorStudent(int tutorId, int studentId, boolean active, BigDecimal default_price, String preferred_days, String preferred_hours, String className, String notes) {
        this.tutorId = tutorId;
        this.studentId = studentId;
        this.active = true;
        this.start_date = LocalDate.now();
        this.default_price = BigDecimal.ZERO;
        this.preferred_days = "";
        this.preferred_hours = "";
        this.className = "";
        this.notes = "";
        this.created_at = LocalDateTime.now();
        this.last_contacted_at = null;
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

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public BigDecimal getDefault_price() {
        return default_price;
    }

    public void setDefault_price(BigDecimal default_price) {
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

    public String getclassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getLastContactedAt() {
        return last_contacted_at;
    }

    public void setLastContactedAt(LocalDateTime last_contacted_at) {
        this.last_contacted_at = last_contacted_at;
    }

    public String getDefault_name() {
        return default_name;
    }

    public void setDefault_name(String default_name) {
        this.default_name = default_name;
    }

    @Override
    public String toString() {
        return "TutorStudent{ tutorId=" + tutorId + ", studentId=" + studentId + ", student's name=" + default_name + '}';
    }
}
