package org.example;

public class Repay {
    public int id;
    public String lib_card_id, book_id, type_of_repay, end_date;

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

    public String getType_of_repay() {
        return type_of_repay;
    }

    public void setType_of_repay(String type_of_repay) {
        this.type_of_repay = type_of_repay;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public Repay(int id, String lib_card_id, String book_id, String type_of_repay, String end_date) {
        this.id = id;
        this.lib_card_id = lib_card_id;
        this.book_id = book_id;
        this.type_of_repay = type_of_repay;
        this.end_date = end_date;
    }
}
