package com.smartlibrary.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Calculates overdue fine using tiered slab rules.
 *
 * Rules:
 *  - ≤ 0 days overdue  → ₹0
 *  - 1 to 7 days       → ₹5 per day
 *  - > 7 days          → ₹5/day for first 7 days + ₹10/day thereafter
 */
public final class FineCalculator {

    private FineCalculator() {} // utility class

    public static double calculateFine(LocalDate dueDate, LocalDate returnDate) {
        if (dueDate == null || returnDate == null) {
            return 0.0;
        }

        long overdueDays = ChronoUnit.DAYS.between(dueDate, returnDate);

        if (overdueDays <= 0) {
            return 0.0;
        }

        if (overdueDays <= 7) {
            return overdueDays * 5.0;
        }

        // First 7 days @ ₹5 + remaining days @ ₹10
        return (7 * 5.0) + ((overdueDays - 7) * 10.0);
    }

    /**
     * Helper to show fine breakdown as a readable string.
     */
    public static String getFineBreakdown(LocalDate dueDate, LocalDate returnDate) {
        long overdueDays = ChronoUnit.DAYS.between(dueDate, returnDate);
        if (overdueDays <= 0) {
            return "Returned on time. Fine = ₹0";
        }
        if (overdueDays <= 7) {
            return String.format("%d day(s) overdue × ₹5 = ₹%.2f", overdueDays, overdueDays * 5.0);
        }
        double fine = (7 * 5.0) + ((overdueDays - 7) * 10.0);
        return String.format("7 days × ₹5 + %d days × ₹10 = ₹%.2f", (overdueDays - 7), fine);
    }
}
