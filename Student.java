package org.example;

public class Student extends Reader {
    public String group_num;
    public String department;
    public Student(int id, String first_name, String last_name, String from_uni, String type,
                   String group_num, String department) {
        super(id, first_name, last_name, from_uni, type);
        this.group_num = group_num;
        this.department = department;
    }

    public String getGroup_num() {
        return group_num;
    }

    public void setGroup_num(String group_num) {
        this.group_num = group_num;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
