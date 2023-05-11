package org.example;

public class Reader {
    public Reader(int id, String first_name, String last_name, String from_uni, String type) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.from_uni = from_uni;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;
    String first_name;
    String last_name;
    String from_uni;
    String type;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFrom_uni() {
        return from_uni;
    }

    public void setFrom_uni(String from_uni) {
        this.from_uni = from_uni;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
