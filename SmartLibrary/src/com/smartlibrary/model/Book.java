package com.smartlibrary.model;

import java.io.Serializable;

/**
 * Domain model representing a Book in the library.
 */
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    private String isbn;
    private String title;
    private String author;
    private String category;
    private boolean available;

    public Book() {}

    public Book(String isbn, String title, String author, String category, boolean available) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.available = available;
    }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return String.format("%-15s | %-30s | %-20s | %-15s | %s",
                isbn, title, author, category, available ? "AVAILABLE" : "ISSUED");
    }
}
