package org.example;

public class Request5 {
    public String locID, countReader, countDead;

    public String getLocID() {
        return locID;
    }

    public void setLocID(String locID) {
        this.locID = locID;
    }

    public String getCountReader() {
        return countReader;
    }

    public void setCountReader(String countReader) {
        this.countReader = countReader;
    }

    public String getCountDead() {
        return countDead;
    }

    public void setCountDead(String countDead) {
        this.countDead = countDead;
    }

    public Request5(String locID, String countReader, String countDead) {
        this.locID = locID;
        this.countReader = countReader;
        this.countDead = countDead;
    }
}
