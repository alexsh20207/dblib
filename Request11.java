package org.example;

public class Request11 {
    public String name, locId, count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Request11(String name, String locId, String count) {
        this.name = name;
        this.locId = locId;
        this.count = count;
    }
}
