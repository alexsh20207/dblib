package org.example;

public class RuleBreaker {
    public int id;
    public String lib_card_id, reason, end_date;

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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public RuleBreaker(int id, String lib_card_id, String reason, String end_date) {
        this.id = id;
        this.lib_card_id = lib_card_id;
        this.reason = reason;
        this.end_date = end_date;
    }
}
