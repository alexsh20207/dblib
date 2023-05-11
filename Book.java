package org.example;

public class Book {
    int id;
    String name, author, edition, date_of_adding, location_id, status, price;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getDate_of_adding() {
        return date_of_adding;
    }

    public void setDate_of_adding(String date_of_adding) {
        this.date_of_adding = date_of_adding;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Book(int id, String name, String author, String price, String date,  String status, String edition, String location) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.edition = edition;
        this.date_of_adding = date;
        this.location_id = location;
        this.status = status;
        this.price = price;
    }
}
