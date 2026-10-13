package com.smartlibrary.repository;

import com.smartlibrary.model.Transaction;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Persists issue/return transactions to a CSV file.
 * Format: TransactionId,ISBN,MemberId,IssueDate,DueDate,ReturnDate,Fine
 */
public class TransactionRepository {

    private final List<Transaction> transactions = new ArrayList<>();
    private final String filePath;
    private int counter = 1;

    public TransactionRepository(String filePath) {
        this.filePath = filePath;
        loadFromCSV();
    }

    public void loadFromCSV() {
        transactions.clear();
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) return;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = reader.readLine(); // header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",", -1);
                if (p.length >= 7) {
                    Transaction t = new Transaction();
                    t.setTransactionId(p[0].trim());
                    t.setIsbn(p[1].trim());
                    t.setMemberId(p[2].trim());
                    t.setIssueDate(LocalDate.parse(p[3].trim()));
                    t.setDueDate(LocalDate.parse(p[4].trim()));
                    if (!p[5].trim().isEmpty() && !p[5].trim().equalsIgnoreCase("PENDING")) {
                        t.setReturnDate(LocalDate.parse(p[5].trim()));
                    }
                    t.setFine(Double.parseDouble(p[6].trim()));
                    transactions.add(t);

                    // keep counter ahead
                    try {
                        int num = Integer.parseInt(p[0].trim().replace("TXN", ""));
                        if (num >= counter) counter = num + 1;
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
        }
    }

    public void saveToCSV() {
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write("TransactionId,ISBN,MemberId,IssueDate,DueDate,ReturnDate,Fine");
                writer.newLine();
                for (Transaction t : transactions) {
                    writer.write(String.format("%s,%s,%s,%s,%s,%s,%.2f",
                            t.getTransactionId(),
                            t.getIsbn(),
                            t.getMemberId(),
                            t.getIssueDate(),
                            t.getDueDate(),
                            t.getReturnDate() == null ? "PENDING" : t.getReturnDate().toString(),
                            t.getFine()));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving transactions: " + e.getMessage());
        }
    }

    public String nextId() {
        return String.format("TXN%04d", counter++);
    }

    public void add(Transaction t) {
        transactions.add(t);
        saveToCSV();
    }

    public void update(Transaction t) {
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getTransactionId().equals(t.getTransactionId())) {
                transactions.set(i, t);
                break;
            }
        }
        saveToCSV();
    }

    public Optional<Transaction> findActiveByIsbnAndMember(String isbn, String memberId) {
        return transactions.stream()
                .filter(t -> t.getIsbn().equalsIgnoreCase(isbn)
                        && t.getMemberId().equalsIgnoreCase(memberId)
                        && !t.isReturned())
                .findFirst();
    }

    public List<Transaction> findByMember(String memberId) {
        return transactions.stream()
                .filter(t -> t.getMemberId().equalsIgnoreCase(memberId))
                .collect(Collectors.toList());
    }

    public List<Transaction> findAll() {
        return new ArrayList<>(transactions);
    }

    public List<Transaction> findActive() {
        return transactions.stream().filter(t -> !t.isReturned()).collect(Collectors.toList());
    }
}
