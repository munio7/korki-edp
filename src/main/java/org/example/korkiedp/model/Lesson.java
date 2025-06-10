package org.example.korkiedp.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Lesson {
    private int id;
    private int tutorId;
    private int studentId;
    private LocalDate date;
    private LocalTime startTime;
    private int durationMinutes;
    private BigDecimal price;
    private boolean paid;
    private boolean attendance; // can be null if not marked yet
    private boolean canceled;
    private String cancelReason;
    private int updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String topic;
    private String subject;



    //Empty
    public Lesson() {}

    //Register
    public Lesson(int tutorId, int studentId, LocalDate date, LocalTime startTime){
        this.tutorId = tutorId;
        this.studentId = studentId;
        this.date = date;
        this.startTime = startTime;
        this.paid = false;
        this.attendance = false;
        this.canceled = false;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    //Full
    public Lesson(int tutorId, int studentId, LocalDate date, LocalTime startTime, int durationMinutes,
                  BigDecimal price,String topic, String subject) {
        this.tutorId = tutorId;
        this.studentId = studentId;
        this.date = date;
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
        this.price = price;
        this.paid = false;
        this.attendance = false;
        this.canceled = false;
        this.cancelReason = "";
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
        this.updatedBy = tutorId;
        this.topic = topic;
        this.subject = subject;
    }

    // Getters and setters below...

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTutorId() { return tutorId; }
    public void setTutorId(int tutorId) { this.tutorId = tutorId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }

    public Boolean getAttendance() { return attendance; }
    public void setAttendance(Boolean attendance) { this.attendance = attendance; }

    public boolean isCanceled() { return canceled; }
    public void setCanceled(boolean canceled) { this.canceled = canceled; }

    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }

    public int getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(int updatedBy) { this.updatedBy = updatedBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
}
