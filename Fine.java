package org.example;

public class Fine {
    public int id;
    public String lib_card_id, fine;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLib_card_id() {
        return lib_card_id;
    }

    public void setLib_card_id(String lib_card_id) {
        this.lib_card_id = lib_card_id;
    }

    public String getFine() {
        return fine;
    }

    public void setFine(String fine) {
        this.fine = fine;
    }

    public Fine(int id, String lib_card_id, String fine) {
        this.id = id;
        this.lib_card_id = lib_card_id;
        this.fine = fine;
    }
}
