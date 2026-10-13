package com.smartlibrary.repository;

import com.smartlibrary.model.Member;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles all Member data access using CSV file persistence.
 * Format: MemberId,Name,Type,IssuedBooks (pipe-separated ISBNs)
 */
public class MemberRepository {

    private final List<Member> members = new ArrayList<>();
    private final String filePath;

    public MemberRepository(String filePath) {
        this.filePath = filePath;
        loadFromCSV();
    }

    public void loadFromCSV() {
        members.clear();
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
                if (parts.length >= 3) {
                    Member m = new Member(parts[0].trim(), parts[1].trim(), parts[2].trim());
                    if (parts.length >= 4 && !parts[3].trim().isEmpty()) {
                        String[] isbns = parts[3].trim().split("\\|");
                        for (String isbn : isbns) {
                            if (!isbn.isEmpty()) m.getIssuedBooks().add(isbn.trim());
                        }
                    }
                    members.add(m);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading members: " + e.getMessage());
            createSampleData();
        }
    }

    public void saveToCSV() {
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write("MemberId,Name,Type,IssuedBooks");
                writer.newLine();
                for (Member m : members) {
                    String issued = String.join("|", m.getIssuedBooks());
                    writer.write(String.format("%s,%s,%s,%s",
                            m.getMemberId(), m.getName(), m.getType(), issued));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving members: " + e.getMessage());
        }
    }

    private void createSampleData() {
        members.add(new Member("M001", "Ananya Sharma", "STUDENT"));
        members.add(new Member("M002", "Rohan Mehta", "STUDENT"));
        members.add(new Member("M003", "Dr. Priya Nair", "FACULTY"));
        members.add(new Member("M004", "Vikram Singh", "STUDENT"));
        members.add(new Member("M005", "Prof. Kabir Rao", "FACULTY"));
    }

    public Optional<Member> findById(String memberId) {
        return members.stream()
                .filter(m -> m.getMemberId().equalsIgnoreCase(memberId))
                .findFirst();
    }

    public List<Member> findAll() {
        return new ArrayList<>(members);
    }

    public void update(Member member) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getMemberId().equals(member.getMemberId())) {
                members.set(i, member);
                break;
            }
        }
        saveToCSV();
    }

    public void add(Member member) {
        members.add(member);
        saveToCSV();
    }

    public int count() {
        return members.size();
    }
}
