package org.example;

public class Request12 {
    public String readerId, firstName, lastName, date;

    public String getReaderId() {
        return readerId;
    }

    public void setReaderId(String readerId) {
        this.readerId = readerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Request12(String readerId, String firstName, String lastName, String date) {
        this.readerId = readerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
    }
}
