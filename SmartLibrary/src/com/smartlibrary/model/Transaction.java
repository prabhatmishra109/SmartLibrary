package com.smartlibrary.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Records a book issue / return transaction.
 */
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    private String transactionId;
    private String isbn;
    private String memberId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;   // null if not yet returned
    private double fine;

    public Transaction() {}

    public Transaction(String transactionId, String isbn, String memberId,
                       LocalDate issueDate, LocalDate dueDate) {
        this.transactionId = transactionId;
        this.isbn = isbn;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = null;
        this.fine = 0.0;
    }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public double getFine() { return fine; }
    public void setFine(double fine) { this.fine = fine; }

    public boolean isReturned() {
        return returnDate != null;
    }

    @Override
    public String toString() {
        return String.format("%-12s | ISBN: %-15s | Member: %-10s | Issue: %s | Due: %s | Return: %s | Fine: ₹%.2f",
                transactionId, isbn, memberId, issueDate, dueDate,
                returnDate == null ? "PENDING" : returnDate.toString(), fine);
    }
}
