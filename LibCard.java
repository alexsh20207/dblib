package org.example;

public class LibCard {
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String start_date;
    public String reader_id;
    public String location_id;
    public String last_registration;
    public String is_abonement_allowed;

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getReader_id() {
        return reader_id;
    }

    public void setReader_id(String reader_id) {
        this.reader_id = reader_id;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getLast_registration() {
        return last_registration;
    }

    public void setLast_registration(String last_registration) {
        this.last_registration = last_registration;
    }

    public String getIs_abonement_allowed() {
        return is_abonement_allowed;
    }

    public void setIs_abonement_allowed(String is_abonement_allowed) {
        this.is_abonement_allowed = is_abonement_allowed;
    }

    public LibCard(int anInt, String startDate, String readerId, String locationId, String lastReg, String isAbonAllowed) {
        this.id = anInt;
        this.start_date = startDate;
        this.reader_id = readerId;
        this.location_id = locationId;
        this.last_registration = lastReg;
        this.is_abonement_allowed = isAbonAllowed;
    }
}
