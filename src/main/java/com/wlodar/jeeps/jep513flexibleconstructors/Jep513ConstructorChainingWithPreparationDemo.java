package com.wlodar.jeeps.jep513flexibleconstructors;

import com.wlodar.WorkshopPrinter;

public class Jep513ConstructorChainingWithPreparationDemo {

    static void main() {
        WorkshopPrinter.title("JEP 513 - preparation before this(...)");

        var account = new UserAccountWithNormalization("  Premium-User  ");

        WorkshopPrinter.print("Login", account.login());
        WorkshopPrinter.print("Role", account.role());
    }
}

/**
 * Demonstrates constructor chaining using this(...)
 * with validation and normalization performed BEFORE the chaining call.
 */
record UserAccountWithNormalization(String login, String role) {

    /**
     * Entry constructor that accepts raw user input.
     * <p>
     * Before JEP 513:
     * - this(...) had to be the first statement
     * - validation/normalization had to be extracted into static helper methods
     * <p>
     * After JEP 513:
     * - we can execute logic before calling this(...)
     * - the constructor can contain full preparation logic
     */
    UserAccountWithNormalization(String rawLogin) {

        WorkshopPrinter.print("Step 1", "Constructor entered with raw input");

        // Validation happens directly in the constructor
        if (rawLogin == null || rawLogin.isBlank()) {
            throw new IllegalArgumentException("Login cannot be blank");
        }

        // Normalize input before passing it further
        // Typical real-life scenario: trimming and lowercasing user input
        rawLogin = rawLogin.trim().toLowerCase();

        WorkshopPrinter.print("Step 2", "Normalized login: " + rawLogin);

        // Constructor chaining happens AFTER preparation
        // We pass already validated and normalized values
        this(rawLogin, "USER");

        // Important note:
        // At this point, the object is still under construction.
        // The language still restricts how 'this' can be used before the chaining call.
    }

    /**
     * Target constructor responsible for assigning fields.
     */
    UserAccountWithNormalization {
        WorkshopPrinter.print("Step 3", "Inside target constructor");

    }
}
