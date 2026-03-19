package com.wlodar.jeeps.jep513flexibleconstructors;

import com.wlodar.WorkshopPrinter;

public class Jep513BeforeValidationDemo {

    static void main() {
        WorkshopPrinter.title("JEP 513 - before");

        var user = new ValidatedUserBefore("   user@example.com   ");
        WorkshopPrinter.print("Email", user.email());
    }
}

class UserBaseBefore {
    private final String email;

    UserBaseBefore(String email) {
        this.email = email;
    }

    String email() {
        return email;
    }
}

class ValidatedUserBefore extends UserBaseBefore {

    ValidatedUserBefore(String rawEmail) {
        //nothing bad in static method but now we are not forced to write everything
        // in one line
        //var normalized = normalizeAndValidate(rawEmail);
        super(normalizeAndValidate(rawEmail));
    }

    private static String normalizeAndValidate(String rawEmail) {
        WorkshopPrinter.print("Normalizing and validating in helper method");

        if (rawEmail == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }

        var normalized = rawEmail.trim().toLowerCase();

        if (!normalized.contains("@")) {
            throw new IllegalArgumentException("Invalid email: " + rawEmail);
        }

        return normalized;
    }
}
