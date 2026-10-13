package com.smartlibrary.service;

import com.smartlibrary.exception.*;
import com.smartlibrary.model.*;
import com.smartlibrary.repository.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Core business logic for the SmartLibrary system.
 */
public class LibraryService {

    private final BookRepository bookRepo;
    private final MemberRepository memberRepo;
    private final TransactionRepository txnRepo;

    public LibraryService() {
        // Paths relative to project root (where you run the app)
        this.bookRepo = new BookRepository("data/books.csv");
        this.memberRepo = new MemberRepository("data/members.csv");
        this.txnRepo = new TransactionRepository("data/transactions.csv");
    }

    // ───────────────────── Book Operations ─────────────────────

    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    public List<Book> getAvailableBooks() {
        return bookRepo.findAvailable();
    }

    public List<Book> searchBooks(String keyword) {
        return bookRepo.search(keyword);
    }

    public Optional<Book> findBook(String isbn) {
        return bookRepo.findByIsbn(isbn);
    }

    // ───────────────────── Member Operations ─────────────────────

    public List<Member> getAllMembers() {
        return memberRepo.findAll();
    }

    public Optional<Member> findMember(String memberId) {
        return memberRepo.findById(memberId);
    }

    // ───────────────────── Issue Book ─────────────────────

    public Transaction issueBook(String isbn, String memberId) throws Exception {
        Book book = bookRepo.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book with ISBN " + isbn + " not found."));

        if (!book.isAvailable()) {
            throw new BookAlreadyIssuedException("Book \"" + book.getTitle() + "\" is already issued.");
        }

        Member member = memberRepo.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member " + memberId + " not found."));

        if (!member.canBorrow()) {
            throw new MemberLimitExceededException(
                    member.getName() + " has already borrowed " + member.getMaxBooks() +
                            " books (limit for " + member.getType() + ").");
        }

        // Perform issue
        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(14);

        book.setAvailable(false);
        member.getIssuedBooks().add(isbn);

        Transaction txn = new Transaction(txnRepo.nextId(), isbn, memberId, issueDate, dueDate);

        bookRepo.update(book);
        memberRepo.update(member);
        txnRepo.add(txn);

        return txn;
    }

    // ───────────────────── Return Book ─────────────────────

    public Transaction returnBook(String isbn, String memberId) throws Exception {
        Book book = bookRepo.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book with ISBN " + isbn + " not found."));

        Member member = memberRepo.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member " + memberId + " not found."));

        Transaction txn = txnRepo.findActiveByIsbnAndMember(isbn, memberId)
                .orElseThrow(() -> new BookNotFoundException(
                        "No active issue found for ISBN " + isbn + " and member " + memberId));

        LocalDate returnDate = LocalDate.now();
        double fine = FineCalculator.calculateFine(txn.getDueDate(), returnDate);

        // Update states
        book.setAvailable(true);
        member.getIssuedBooks().remove(isbn);

        txn.setReturnDate(returnDate);
        txn.setFine(fine);

        bookRepo.update(book);
        memberRepo.update(member);
        txnRepo.update(txn);

        return txn;
    }

    // ───────────────────── Reports ─────────────────────

    public List<Transaction> getActiveTransactions() {
        return txnRepo.findActive();
    }

    public List<Transaction> getMemberHistory(String memberId) {
        return txnRepo.findByMember(memberId);
    }

    public List<Transaction> getAllTransactions() {
        return txnRepo.findAll();
    }

    public void printStats() {
        System.out.println("\n── Library Statistics ──");
        System.out.println("Total Books     : " + bookRepo.count());
        System.out.println("Available Books : " + bookRepo.findAvailable().size());
        System.out.println("Total Members   : " + memberRepo.count());
        System.out.println("Active Issues   : " + txnRepo.findActive().size());
    }
}
