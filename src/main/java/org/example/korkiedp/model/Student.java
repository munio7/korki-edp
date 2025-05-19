package org.example.korkiedp.model;

import java.time.LocalDateTime;

public class Student {
    private int id;
    private String name;

    // Empty
    public Student() {}

    // Register
    public Student(String name) {
        this.name = name;
    }

    // Full
    public Student(int id, String name) {
        this.id = id;
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
