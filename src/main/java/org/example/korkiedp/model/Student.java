package org.example.korkiedp.model;

public class Student {
    private int id;
    private String firstName;
    private String lastName;
    private String localization;
    private String telNumber;

    // Empty
    public Student() {}

    // Register
    public Student(String firsName, String lastName) {
        this.firstName = firsName;
        this.lastName = lastName;
    }

    // Full
    public Student(int id,String firsName, String lastName, String localization, String telNumber) {
        this.id = id;
        this.firstName = firsName;
        this.lastName = lastName;
        this.localization = localization;
        this.telNumber = telNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

}
