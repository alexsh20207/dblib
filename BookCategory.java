package org.example;

public class BookCategory {
    int id;

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

    String name;
    public BookCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
