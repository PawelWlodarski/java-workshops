package com.wlodar.jeeps.jep394;

import com.wlodar.WorkshopPrinter;

public class JEP394PatternMatchingInstanceofDemo {

    static void main() {
        WorkshopPrinter.title("JEP 394: Pattern Matching for instanceof");

        oldStyleInstanceof();
        newStyleInstanceof();
        orVsAndDemo();
    }

    static void oldStyleInstanceof() {
        WorkshopPrinter.subtitle("Before JEP 394");

        Object value = "  Java Pre 16  ";

        if (value instanceof String) {
            String text = (String) value; // manual cast
            WorkshopPrinter.print("Result", text.trim().toUpperCase());
        }
    }

    static void newStyleInstanceof() {
        WorkshopPrinter.subtitle("With pattern matching");

        Object value = "  Java 16+  ";

        if (value instanceof String text) {
            WorkshopPrinter.print("Result", text.trim().toUpperCase());
        }

        Object number = 42;

        if (number instanceof String text) {
            WorkshopPrinter.print("Will not print", text);
        } else {
            WorkshopPrinter.print("Number is not a String");
        }
    }

    static void orVsAndDemo() {
        WorkshopPrinter.subtitle("|| vs && with pattern variables");

        Object value = "hello";

        // --- DOES NOT COMPILE ---
        // Uncomment to show compiler error
    /*
    if (value instanceof String text || text.length() > 5) {
        WorkshopPrinter.print("OR case", text);
    }
    */

        // With ||, the right side may execute even if the instanceof check is false,
        // so 'text' might not exist → compiler error


        // --- WORKS ---
        if (value instanceof String text && text.length() > 3) {
            WorkshopPrinter.print("AND case", text.toUpperCase());
        }

        // Explanation:
        // With &&, right side executes only if instanceof matched,
        // so 'text' is guaranteed to exist
    }
}
