package org.example;

public class BookInCategory {
    public int id;
    public String book_id;

    public BookInCategory(int id, String book_id, String category_id) {
        this.id = id;
        this.book_id = book_id;
        this.category_id = category_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String category_id;
}
