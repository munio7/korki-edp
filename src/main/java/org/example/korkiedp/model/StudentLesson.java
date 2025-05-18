package org.example.korkiedp.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class StudentLesson {
    private int id;
    private int tutor_id;
    private int student_id;
    private LocalDate date;
    private LocalTime start_time;
    private int duration_minutes;
    private BigDecimal price;
    private boolean paid;
    private boolean attendance;
    private boolean cancelled;
    private String cancel_reason;
    private int updated_by;
    private LocalDateTime updated_at;
    private LocalDateTime modified_at;
    private String topic;

    public StudentLesson() {}

    public StudentLesson(int student_id, int tutor_id, LocalDate date, int duration_minutes, BigDecimal price, String topic) {
        this.student_id = student_id;
        this.tutor_id = tutor_id;
        this.date = date;
        this.duration_minutes = duration_minutes;
        this.price = price;
        this.topic = topic;
        this.paid = false;
    }

    // --- Gettery i settery ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return student_id;
    }

    public void setStudentId(int student_id) {
        this.student_id = student_id;
    }

    public int getTutorId() {
        return tutor_id;
    }

    public void setTutorId(int tutor_id) {
        this.tutor_id = tutor_id;
    }

    public LocalTime getStartTime() {
        return start_time;
    }

    public void setStartTime(LocalTime start_time) {
        this.start_time = start_time;
    }

    public boolean isAttendance() {
        return attendance;
    }

    public void setAttendance(boolean attendance) {
        this.attendance = attendance;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getCancelReason() {
        return cancel_reason;
    }

    public void setCancelReason(String cancel_reason) {
        this.cancel_reason = cancel_reason;
    }

    public int getUpdatedBy() {
        return updated_by;
    }

    public void setUpdatedBy(int updated_by) {
        this.updated_by = updated_by;
    }

    public LocalDateTime getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public LocalDateTime getModifiedAt() {
        return modified_at;
    }

    public void setModifiedAt(LocalDateTime modified_at) {
        this.modified_at = modified_at;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getDurationMinutes() {
        return duration_minutes;
    }

    public void setDurationMinutes(int duration_minutes) {
        this.duration_minutes = duration_minutes;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return topic + " (" + date + ", " + start_time + ") - " + (paid ? "zapłacono" : "niezapłacono");
    }
}
