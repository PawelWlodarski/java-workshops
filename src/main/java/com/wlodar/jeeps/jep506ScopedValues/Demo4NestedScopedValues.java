package com.wlodar.jeeps.jep506ScopedValues;

import com.wlodar.WorkshopPrinter;

public class Demo4NestedScopedValues {
    private static final ScopedValue<String> USER = ScopedValue.newInstance();

    static void main() {

        WorkshopPrinter.title("ScopedValue - nested bindings");

        WorkshopPrinter.subtitle("Outer scope");

        ScopedValue.where(USER, "alice").run(() -> {

            WorkshopPrinter.print("USER", USER.get());

            innerScope();

            WorkshopPrinter.print("USER after inner scope", USER.get());
        });

        WorkshopPrinter.subtitle("Outside scope");

        WorkshopPrinter.print("USER.isBound()", USER.isBound());
    }

    static void innerScope() {

        WorkshopPrinter.subtitle("Inner scope");

        ScopedValue.where(USER, "mallory").run(() -> {

            WorkshopPrinter.print("USER inside inner scope", USER.get());
        });

        WorkshopPrinter.print("inner scope", "finished");
    }
}
