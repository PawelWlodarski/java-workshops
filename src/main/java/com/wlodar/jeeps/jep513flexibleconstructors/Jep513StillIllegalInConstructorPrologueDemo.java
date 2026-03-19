package com.wlodar.jeeps.jep513flexibleconstructors;

import com.wlodar.WorkshopPrinter;

public class Jep513StillIllegalInConstructorPrologueDemo {

    static void main() {
        WorkshopPrinter.title("JEP 513 - what is still illegal before this(...)");

        var account = new UserAccountConstructorPrologueRulesDemo("  Premium-User  ");

        WorkshopPrinter.print("Login", account.login());
        WorkshopPrinter.print("Role", account.role());
    }
}

record UserAccountConstructorPrologueRulesDemo(String login, String role) {

    UserAccountConstructorPrologueRulesDemo(String rawLogin) {

        WorkshopPrinter.print("Step 1", "Constructor entered with raw input");

        // Legal: validation
        if (rawLogin == null || rawLogin.isBlank()) {
            throw new IllegalArgumentException("Login cannot be blank");
        }

        // Legal: transformation
        rawLogin = rawLogin.trim().toLowerCase();

        WorkshopPrinter.print("Step 2", "Normalized login: " + rawLogin);

        // ILLEGAL: explicit reference to current instance before constructor chaining
//        System.out.println(this);

        // ILLEGAL: accessing instance field requires using current instance
//        System.out.println(this.role);

        // ILLEGAL: instance method call implicitly uses 'this'
//        printDebugMessage();

        // ILLEGAL: implicit 'this' when accessing field without qualifier
//        var currentRole = role;

        // ILLEGAL: calling superclass method still uses current instance
//        System.out.println(super.hashCode());

        // Legal: constructor chaining after preparation
        this(rawLogin, "USER");
    }

    UserAccountConstructorPrologueRulesDemo {
        WorkshopPrinter.print("Step 3", "Inside target constructor");
    }

    private void printDebugMessage() {
        System.out.println("debug");
    }
}
