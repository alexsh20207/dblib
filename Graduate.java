package org.example;

public class Graduate extends Reader {
    String department;
    public Graduate(int id, String first_name, String last_name, String from_uni, String type,
                    String department) {
        super(id, first_name, last_name, from_uni, type);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
