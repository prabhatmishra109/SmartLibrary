package com.smartlibrary.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Domain model representing a library Member (Student or Faculty).
 */
public class Member implements Serializable {
    private static final long serialVersionUID = 1L;

    private String memberId;
    private String name;
    private String type;          // STUDENT or FACULTY
    private List<String> issuedBooks;
    private int maxBooks;

    public Member() {
        this.issuedBooks = new ArrayList<>();
    }

    public Member(String memberId, String name, String type) {
        this.memberId = memberId;
        this.name = name;
        this.type = type.toUpperCase();
        this.issuedBooks = new ArrayList<>();
        this.maxBooks = "FACULTY".equals(this.type) ? 5 : 3;
    }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) {
        this.type = type.toUpperCase();
        this.maxBooks = "FACULTY".equals(this.type) ? 5 : 3;
    }

    public List<String> getIssuedBooks() { return issuedBooks; }
    public void setIssuedBooks(List<String> issuedBooks) { this.issuedBooks = issuedBooks; }

    public int getMaxBooks() { return maxBooks; }

    public boolean canBorrow() {
        return issuedBooks.size() < maxBooks;
    }

    @Override
    public String toString() {
        return String.format("%-10s | %-20s | %-10s | Books issued: %d/%d",
                memberId, name, type, issuedBooks.size(), maxBooks);
    }
}
