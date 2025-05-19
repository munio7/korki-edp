package org.example.korkiedp.model;

import java.time.LocalDateTime;
import java.util.Date;

public class Tutor {
    private int id;
    private String username;
    private String password_hash;
    private String full_name;
    private String email;
    private LocalDateTime created_at;

    // Full
    public Tutor(int id, String username, String password_hash, String full_name, String email, LocalDateTime created_at) {
        this.id = id;
        this.username = username;
        this.password_hash = password_hash;
        this.full_name = full_name;
        this.email = email;
        this.created_at = created_at;
    }

    // Register
    public Tutor(String username, String password_hash, String full_name, String email) {
        this.username = username;
        this.password_hash = password_hash;
        this.full_name = full_name;
        this.email = email;
    }
    // Updating
    public Tutor(int id, String username, String password_hash, String full_name, String email) {
        this.id = id;
        this.username = username;
        this.password_hash = password_hash;
        this.full_name = full_name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return password_hash;
    }

    public void setPasswordHash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getFullName() {
        return full_name;
    }

    public void setFullName(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return full_name + " (" + username + ")";
    }


}

