package org.example;

public class Request4 {
    public String id, name, type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Request4(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
}
