package com.smartlibrary;

import com.smartlibrary.model.*;
import com.smartlibrary.service.FineCalculator;
import com.smartlibrary.service.LibraryService;

import java.util.List;
import java.util.Scanner;

/**
 * SmartLibrary – Console-based Automated Library & Fine Calculation Engine.
 * Pure Core Java (JDK 8+). No external libraries.
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final LibraryService service = new LibraryService();

    public static void main(String[] args) {
        printBanner();
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Enter choice: ");
            String input = sc.nextLine().trim();

            try {
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1 -> viewAllBooks();
                    case 2 -> searchBooks();
                    case 3 -> viewAvailableBooks();
                    case 4 -> issueBook();
                    case 5 -> returnBook();
                    case 6 -> viewMembers();
                    case 7 -> viewMemberDetails();
                    case 8 -> viewActiveIssues();
                    case 9 -> viewTransactionHistory();
                    case 10 -> service.printStats();
                    case 0 -> {
                        System.out.println("\nSaving data... Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("\n⚠ Error: " + e.getMessage());
            }
        }
        sc.close();
    }

    private static void printBanner() {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║     SMART LIBRARY – Automated Fine Engine        ║");
        System.out.println("║          Pure Core Java  |  CSV Persistence      ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
    }

    private static void printMenu() {
        System.out.println("\n══════════════ MAIN MENU ══════════════");
        System.out.println(" 1. View All Books");
        System.out.println(" 2. Search Books");
        System.out.println(" 3. View Available Books");
        System.out.println(" 4. Issue Book");
        System.out.println(" 5. Return Book");
        System.out.println(" 6. View All Members");
        System.out.println(" 7. View Member Details / History");
        System.out.println(" 8. View Active Issues");
        System.out.println(" 9. View All Transactions");
        System.out.println("10. Library Statistics");
        System.out.println(" 0. Exit");
        System.out.println("═══════════════════════════════════════");
    }

    private static void viewAllBooks() {
        List<Book> books = service.getAllBooks();
        printBookHeader();
        books.forEach(System.out::println);
        System.out.println("Total: " + books.size() + " books");
    }

    private static void viewAvailableBooks() {
        List<Book> books = service.getAvailableBooks();
        printBookHeader();
        books.forEach(System.out::println);
        System.out.println("Available: " + books.size() + " books");
    }

    private static void searchBooks() {
        System.out.print("Enter keyword (title / author / category / ISBN): ");
        String keyword = sc.nextLine().trim();
        List<Book> results = service.searchBooks(keyword);
        if (results.isEmpty()) {
            System.out.println("No books matched \"" + keyword + "\".");
        } else {
            printBookHeader();
            results.forEach(System.out::println);
            System.out.println("Found: " + results.size());
        }
    }

    private static void issueBook() throws Exception {
        System.out.print("Enter ISBN: ");
        String isbn = sc.nextLine().trim();
        System.out.print("Enter Member ID: ");
        String memberId = sc.nextLine().trim();

        Transaction txn = service.issueBook(isbn, memberId);
        System.out.println("\n✓ Book issued successfully!");
        System.out.println("  Transaction ID : " + txn.getTransactionId());
        System.out.println("  Issue Date     : " + txn.getIssueDate());
        System.out.println("  Due Date       : " + txn.getDueDate() + " (14 days)");
    }

    private static void returnBook() throws Exception {
        System.out.print("Enter ISBN: ");
        String isbn = sc.nextLine().trim();
        System.out.print("Enter Member ID: ");
        String memberId = sc.nextLine().trim();

        Transaction txn = service.returnBook(isbn, memberId);
        System.out.println("\n✓ Book returned successfully!");
        System.out.println("  Transaction ID : " + txn.getTransactionId());
        System.out.println("  Due Date       : " + txn.getDueDate());
        System.out.println("  Return Date    : " + txn.getReturnDate());
        System.out.println("  " + FineCalculator.getFineBreakdown(txn.getDueDate(), txn.getReturnDate()));
        System.out.printf("  Total Fine     : ₹%.2f%n", txn.getFine());
    }

    private static void viewMembers() {
        List<Member> members = service.getAllMembers();
        System.out.println("\nMemberId   | Name                 | Type       | Status");
        System.out.println("-----------|----------------------|------------|------------------");
        members.forEach(System.out::println);
    }

    private static void viewMemberDetails() {
        System.out.print("Enter Member ID: ");
        String memberId = sc.nextLine().trim();
        Member m = service.findMember(memberId)
                .orElse(null);
        if (m == null) {
            System.out.println("Member not found.");
            return;
        }
        System.out.println("\n── Member Profile ──");
        System.out.println(m);
        System.out.println("Currently issued ISBNs: " + m.getIssuedBooks());

        List<Transaction> history = service.getMemberHistory(memberId);
        if (history.isEmpty()) {
            System.out.println("No transaction history.");
        } else {
            System.out.println("\n── Transaction History ──");
            history.forEach(System.out::println);
        }
    }

    private static void viewActiveIssues() {
        List<Transaction> active = service.getActiveTransactions();
        if (active.isEmpty()) {
            System.out.println("No active issues.");
            return;
        }
        System.out.println("\n── Active Issues ──");
        active.forEach(System.out::println);
    }

    private static void viewTransactionHistory() {
        List<Transaction> all = service.getAllTransactions();
        if (all.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }
        System.out.println("\n── All Transactions ──");
        all.forEach(System.out::println);
    }

    private static void printBookHeader() {
        System.out.println("\nISBN            | Title                          | Author               | Category        | Status");
        System.out.println("----------------|--------------------------------|----------------------|-----------------|----------");
    }
}
