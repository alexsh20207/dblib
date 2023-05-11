package org.example;

public class Operation {
    public int id;
    public String lib_card_id, book_id, status, date, loc_id;

    public Operation(int id, String lib_card_id, String book_id, String loc_id, String status, String date) {
        this.id = id;
        this.lib_card_id = lib_card_id;
        this.book_id = book_id;
        this.status = status;
        this.date = date;
        this.loc_id = loc_id;
    }

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

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLoc_id() {
        return loc_id;
    }

    public void setLoc_id(String loc_id) {
        this.loc_id = loc_id;
    }
}
