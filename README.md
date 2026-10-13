# 📚 SmartLibrary — Automated Library & Fine Calculation Engine

A robust, console-based library management and inventory tracking system built strictly using **pure Core Java (JDK 8+)**. This project demonstrates advanced object-oriented programming, data structures, concurrency, and file persistence without relying on any external frameworks (like Spring Boot) or databases.

---

## 🚀 Key Features

* **📦 CSV-Backed Persistence:** Automatically reads and writes book catalogs and member records to local CSV files (`books.csv`, `members.csv`), ensuring data persists across sessions.
* **🔍 Advanced Stream API Search:** Filter books instantly by title, author, category, or ISBN using Java 8 Streams and Lambda expressions.
* **⏱️ Automated Fine Calculation Engine:** Uses the modern `java.time` API (`LocalDate` and `ChronoUnit`) to calculate exact overdue days and compute tiered late fines automatically.
* **👥 Role-Based Borrowing Limits:** Manages member profiles with distinct borrowing rules (e.g., Students can borrow up to 3 books for 14 days; Faculty up to 5 books for 30 days).
* **⚡ Multithreaded Background Alerts:** Utilizes `ScheduledExecutorService` to run asynchronous background checks for overdue books and trigger reminders.
* **🛡️ Robust Exception Handling:** Custom business exception handling to prevent invalid issues, duplicate entries, and out-of-limit borrowings.

---

## 🛠️ Core Java Concepts Implemented

* **Object-Oriented Programming (OOP):** Encapsulation, Inheritance, Polymorphism, and modular package architecture.
* **Collections Framework:** `ArrayList`, `HashMap`, `List`, and `Map` for high-performance in-memory data management.
* **Java Date-Time API (`java.time`):** `LocalDate`, `Period`, and `ChronoUnit.DAYS` for accurate date arithmetic.
* **Concurrency & Multithreading:** `ScheduledExecutorService` for background task scheduling.
* **File I/O:** `BufferedReader`, `BufferedWriter`, and `FileReader`/`FileWriter` for structured CSV parsing.

---

## 📂 Project Structure

```text
SmartLibrary/
│
├── com/smartlibrary/
│   ├── model/
│   │   ├── Book.java          # Encapsulated Book POJO
│   │   ├── Member.java        # Member profile with role limits
│   │   └── Transaction.java   # Issue & return transaction record
│   ├── service/
│   │   ├── LibraryService.java # Core business logic & CSV handler
│   │   └── FineCalculator.java # Overdue fine computation rules
│   └── Main.java              # Interactive console menu & controller
│
├── books.csv                  # Persistent book inventory
└── members.csv                # Persistent member registry
