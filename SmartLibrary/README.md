# SmartLibrary – Automated Library & Fine Calculation Engine

A complete **Core Java** console application for managing a library: books, members, issue/return workflow, and tiered fine calculation.

**No external libraries. No database.** Uses pure Java 8+ features + CSV file persistence.

---

## Features

| Feature | Description |
|---------|-------------|
| Book Management | View, search (Stream API), availability tracking |
| Member Registry | Student (max 3 books) / Faculty (max 5 books) |
| Issue Book | Availability check + borrowing limit + 14-day due date |
| Return Book | Automatic fine calculation with tiered slabs |
| Transaction History | Full issue/return log with fine amounts |
| CSV Persistence | Auto-loads & saves `books.csv`, `members.csv`, `transactions.csv` |
| Custom Exceptions | Clean business-rule error handling |
| Statistics | Quick library overview |

---

## Fine Calculation Rules

```
Overdue Days = ChronoUnit.DAYS.between(dueDate, returnDate)

≤ 0 days   →  ₹0
1–7 days   →  ₹5 per day
> 7 days   →  ₹5/day for first 7 days + ₹10/day for remaining days
```

---

## Project Structure

```
SmartLibrary/
├── src/com/smartlibrary/
│   ├── model/           Book, Member, Transaction
│   ├── repository/      BookRepository, MemberRepository, TransactionRepository
│   ├── service/         LibraryService, FineCalculator
│   ├── exception/       Custom checked exceptions
│   └── Main.java        Console UI
├── data/                CSV files (auto-created on first run)
└── README.md
```

---

## How to Compile & Run

### Option 1 – From project root

```bash
# Compile
javac -d bin src/com/smartlibrary/model/*.java \
             src/com/smartlibrary/exception/*.java \
             src/com/smartlibrary/repository/*.java \
             src/com/smartlibrary/service/*.java \
             src/com/smartlibrary/Main.java

# Run (from project root so data/ path works)
java -cp bin com.smartlibrary.Main
```

### Option 2 – Using an IDE
- Import as a plain Java project
- Set source root to `src`
- Run `com.smartlibrary.Main`

---

## Sample Data (auto-created)

**Books**
- Effective Java, Head First Java, Clean Code, Design Patterns, etc.

**Members**
- M001 Ananya Sharma (STUDENT)
- M002 Rohan Mehta (STUDENT)
- M003 Dr. Priya Nair (FACULTY)
- M004 Vikram Singh (STUDENT)
- M005 Prof. Kabir Rao (FACULTY)

---

## Core Java Concepts Used

- OOP (Encapsulation, Classes, Objects)
- Inheritance & custom Exceptions
- Collections (`ArrayList`, `Optional`, Streams)
- Java 8 Date-Time API (`LocalDate`, `ChronoUnit`)
- Stream API for search & filtering
- File I/O (`BufferedReader` / `BufferedWriter`, NIO `Files`)
- Packages & layered architecture
- Generics (via `Optional`, `List`)

---

## Quick Demo Flow

1. View All Books → note an ISBN  
2. Issue Book → enter ISBN + Member ID (e.g. `9780134685991` + `M001`)  
3. View Active Issues  
4. Return Book → same ISBN + Member ID → see fine calculation  
5. View Member Details → history appears  

---

Built as a portfolio / academic Core Java project.
