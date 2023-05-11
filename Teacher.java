package org.example;

public class Teacher extends Reader{
    String cafedra;
    String rank;
    String degree;
    public Teacher(int id, String first_name, String last_name, String from_uni, String type,
                   String cafedra, String rank, String degree) {
        super(id, first_name, last_name, from_uni, type);
        this.cafedra = cafedra;
        this.degree = degree;
        this.rank = rank;
    }


    public String getCafedra() {
        return cafedra;
    }

    public void setCafedra(String cafedra) {
        this.cafedra = cafedra;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
}
