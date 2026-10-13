package com.smartlibrary.repository;

import com.smartlibrary.model.Book;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles all Book data access using CSV file persistence.
 */
public class BookRepository {

    private final List<Book> books = new ArrayList<>();
    private final String filePath;

    public BookRepository(String filePath) {
        this.filePath = filePath;
        loadFromCSV();
    }

    public void loadFromCSV() {
        books.clear();
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            createSampleData();
            saveToCSV();
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length >= 5) {
                    Book book = new Book(
                            parts[0].trim(),
                            parts[1].trim(),
                            parts[2].trim(),
                            parts[3].trim(),
                            Boolean.parseBoolean(parts[4].trim())
                    );
                    books.add(book);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading books: " + e.getMessage());
            createSampleData();
        }
    }

    public void saveToCSV() {
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write("ISBN,Title,Author,Category,Available");
                writer.newLine();
                for (Book b : books) {
                    writer.write(String.format("%s,%s,%s,%s,%s",
                            b.getIsbn(), b.getTitle(), b.getAuthor(),
                            b.getCategory(), b.isAvailable()));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving books: " + e.getMessage());
        }
    }

    private void createSampleData() {
        books.add(new Book("9780134685991", "Effective Java", "Joshua Bloch", "Programming", true));
        books.add(new Book("9780596009205", "Head First Java", "Kathy Sierra", "Programming", true));
        books.add(new Book("9780132350884", "Clean Code", "Robert C. Martin", "Programming", true));
        books.add(new Book("9780201633610", "Design Patterns", "Erich Gamma", "Software Design", true));
        books.add(new Book("9780134685992", "Java Concurrency", "Brian Goetz", "Programming", true));
        books.add(new Book("9781491950357", "Building Microservices", "Sam Newman", "Architecture", true));
        books.add(new Book("9780596517748", "JavaScript Guide", "David Flanagan", "Web", true));
        books.add(new Book("9780321356680", "Effective C++", "Scott Meyers", "Programming", true));
        books.add(new Book("9780134494166", "Clean Architecture", "Robert C. Martin", "Software Design", true));
        books.add(new Book("9781617294945", "Spring in Action", "Craig Walls", "Framework", true));
    }

    public Optional<Book> findByIsbn(String isbn) {
        return books.stream()
                .filter(b -> b.getIsbn().equalsIgnoreCase(isbn))
                .findFirst();
    }

    public List<Book> search(String keyword) {
        String key = keyword.toLowerCase();
        return books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(key)
                        || b.getAuthor().toLowerCase().contains(key)
                        || b.getCategory().toLowerCase().contains(key)
                        || b.getIsbn().toLowerCase().contains(key))
                .collect(Collectors.toList());
    }

    public List<Book> findAll() {
        return new ArrayList<>(books);
    }

    public List<Book> findAvailable() {
        return books.stream().filter(Book::isAvailable).collect(Collectors.toList());
    }

    public void update(Book book) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getIsbn().equals(book.getIsbn())) {
                books.set(i, book);
                break;
            }
        }
        saveToCSV();
    }

    public void add(Book book) {
        books.add(book);
        saveToCSV();
    }

    public int count() {
        return books.size();
    }
}
