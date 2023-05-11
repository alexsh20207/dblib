package org.example;

public class Request6 {
    public String name, date, bookId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public Request6(String bookId, String name, String date) {
        this.name = name;
        this.date = date;
        this.bookId = bookId;
    }
}
