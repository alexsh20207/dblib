package org.example;

public class Trainee extends Reader {
    String job;
    public Trainee(int id, String first_name, String last_name, String from_uni, String type,
                   String job) {
        super(id, first_name, last_name, from_uni, type);
        this.job = job;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
