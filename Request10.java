package org.example;

public class Request10 {
    public String bookId, name, status;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Request10(String bookId, String name, String status) {
        this.bookId = bookId;
        this.name = name;
        this.status = status;
    }
}
