package com.wlodar.jeeps.jep513flexibleconstructors;

import com.wlodar.WorkshopPrinter;

public class Jep513ValidationDemo {

    static void main() {
        WorkshopPrinter.title("JEP 513 - validation before super");

        var user = new ValidatedUser("   user@example.com   ");
        WorkshopPrinter.print("Email", user.email());
    }
}

class UserBase {
    private final String email;

    UserBase(String email) {
        this.email = email;
    }

    String email() {
        return email;
    }
}

class ValidatedUser extends UserBase {

    ValidatedUser(String rawEmail) {
        WorkshopPrinter.print("Inside constructor body before super");

        if (rawEmail == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }

        rawEmail = rawEmail.trim().toLowerCase();

        if (!rawEmail.contains("@")) {
            throw new IllegalArgumentException("Invalid email: " + rawEmail);
        }

        super(rawEmail);
    }
}
