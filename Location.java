package org.example;

public class Location {
    int id;
    String address;
    String type;

    public Location(String address, String type) {
        this.address = address;
        this.type = type;
    }

    public Location(int id, String address, String type) {
        this.id = id;
        this.address = address;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
