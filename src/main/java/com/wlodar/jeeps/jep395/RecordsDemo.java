package com.wlodar.jeeps.jep395;

import com.wlodar.WorkshopPrinter;

import java.util.Objects;

public class RecordsDemo {

    static void main() {
        WorkshopPrinter.title("JEP 395: Records");

        basicRecordDemo();
        compactConstructorDemo();
    }

    static void basicRecordDemo() {
        WorkshopPrinter.subtitle("Less ceremony for immutable data");

        var old1 = new OldPoint(10, 20);
        var old2 = new OldPoint(10, 20);

        WorkshopPrinter.print("Old class toString", old1);
        WorkshopPrinter.print("Old class equals", old1.equals(old2));
        WorkshopPrinter.print("Old class x getter", old1.x());

        var record1 = new Point(10, 20);
        var record2 = new Point(10, 20);

        WorkshopPrinter.print("Record toString", record1);
        WorkshopPrinter.print("Record equals", record1.equals(record2));
        WorkshopPrinter.print("Record x accessor", record1.x());

        WorkshopPrinter.print("""
                
                Record declaration:
                record Point(int x, int y) {}
                
                Generated for us:
                - private final fields
                - canonical constructor
                - accessors: x(), y()
                - equals / hashCode
                - toString
                """);
    }

    static void compactConstructorDemo() {
        WorkshopPrinter.subtitle("Compact constructor: validate and normalize");

        var p1 = new Person("  Alice  ", "  Smith  ");
        var p2 = new Person("Alice", "Smith");

        WorkshopPrinter.print("Normalized person", p1);
        WorkshopPrinter.print("Equals after normalization", p1.equals(p2));
        WorkshopPrinter.print("Full name", p1.fullName());

        try {
            new Person(" ", "Smith");
        } catch (IllegalArgumentException e) {
            WorkshopPrinter.print("Validation error", e.getMessage());
        }

        WorkshopPrinter.print("""
                
                Compact constructor is great when:
                - you want validation
                - you want normalization
                - but still want record simplicity
                """);
    }

    record Point(int x, int y) {
    }

    record Person(String firstName, String lastName) {
        Person {
            firstName = firstName.trim();
            lastName = lastName.trim();

            if (firstName.isBlank() || lastName.isBlank()) {
                throw new IllegalArgumentException("Name cannot be blank");
            }
        }

        String fullName() {
            return firstName + " " + lastName;
        }
    }

    static final class OldPoint {
        private final int x;
        private final int y;

        OldPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int x() {
            return x;
        }

        int y() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof OldPoint other)) {
                return false;
            }
            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "OldPoint[x=%d, y=%d]".formatted(x, y);
        }
    }
}