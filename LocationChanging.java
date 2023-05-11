package org.example;

public class LocationChanging {
    public int id;
    public String lib_card_id, old_loc_id, new_loc_id, date;

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

    public String getOld_loc_id() {
        return old_loc_id;
    }

    public void setOld_loc_id(String old_loc_id) {
        this.old_loc_id = old_loc_id;
    }

    public String getNew_loc_id() {
        return new_loc_id;
    }

    public void setNew_loc_id(String new_loc_id) {
        this.new_loc_id = new_loc_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public LocationChanging(int id, String lib_card_id, String old_loc_id, String new_loc_id, String date) {
        this.id = id;
        this.lib_card_id = lib_card_id;
        this.old_loc_id = old_loc_id;
        this.new_loc_id = new_loc_id;
        this.date = date;
    }
}
