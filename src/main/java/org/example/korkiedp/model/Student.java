package org.example.korkiedp.model;

import java.time.LocalDateTime;

public class Student {
    private int id;
    private String name;

    public Student() {}

    public Student(String name, String email, String phone, String notes) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
