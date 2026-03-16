package com.wlodar.jeeps.jep506ScopedValues;

import com.wlodar.WorkshopPrinter;

public class Demo3ScopedLifetime {
    private static final ScopedValue<String> USER = ScopedValue.newInstance();

    static void main() {
        WorkshopPrinter.title("ScopedValue - scoped lifetime");

        WorkshopPrinter.subtitle("Before binding");
        //ScopeValue state cna be checked at any time - useful; for debug
        WorkshopPrinter.print("USER.isBound()", USER.isBound());
        WorkshopPrinter.print("USER.orElse(\"not bound\")", USER.orElse("not bound"));

        WorkshopPrinter.subtitle("Inside scope");
        ScopedValue.where(USER, "alice").run(() -> {
            WorkshopPrinter.print("USER.isBound()", USER.isBound());
            WorkshopPrinter.print("USER.get()", USER.get());
            WorkshopPrinter.print("USER.orElse(\"not bound\")", USER.orElse("not bound"));
        });

        WorkshopPrinter.subtitle("After scope");
        WorkshopPrinter.print("USER.isBound()", USER.isBound());
        WorkshopPrinter.print("USER.orElse(\"not bound\")", USER.orElse("not bound"));

        WorkshopPrinter.subtitle("Trying USER.get() outside scope");
        try {
            WorkshopPrinter.print("about to call USER.get() outside scope");
            USER.get();
        } catch (Exception e) {
            WorkshopPrinter.print("exception type", e.getClass().getSimpleName());
            WorkshopPrinter.print("exception message", e.getMessage());
        }
    }
}
