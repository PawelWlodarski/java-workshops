package com.wlodar.jeeps.jep506ScopedValues;

import com.wlodar.WorkshopPrinter;

public class Demo2ScopedValueVsThreadLocal {
    private static final ThreadLocal<String> THREAD_LOCAL_USER = new ThreadLocal<>();
    private static final ScopedValue<String> USER = ScopedValue.newInstance();

    static void main() {

        WorkshopPrinter.title("ScopedValue vs ThreadLocal");

        threadLocalExample();
        scopedValueExample();
    }

    // -----------------------------
    // ThreadLocal demo - you can change threadlocal value from inside call chain
    // even if object itself is immutable.
    // And you need to remember to remove the object when you don't need it
    // -----------------------------

    static void threadLocalExample() {

        WorkshopPrinter.subtitle("ThreadLocal - context mutation");

        THREAD_LOCAL_USER.set("alice");

        WorkshopPrinter.print("handleRequest USER", THREAD_LOCAL_USER.get());

        serviceThreadLocal();

        WorkshopPrinter.print("handleRequest after service USER", THREAD_LOCAL_USER.get());

        THREAD_LOCAL_USER.remove();
    }

    static void serviceThreadLocal() {

        WorkshopPrinter.print("service sees USER", THREAD_LOCAL_USER.get());

        evilLibraryCodeThreadLocal();

        WorkshopPrinter.print("service after evilLibraryCode USER", THREAD_LOCAL_USER.get());
    }

    static void evilLibraryCodeThreadLocal() {

        WorkshopPrinter.print("evilLibraryCode", "changing USER to mallory");

        THREAD_LOCAL_USER.set("mallory");
    }

    // -----------------------------
    // ScopedValue demo - you can change scoped value only for inner calls
    // -----------------------------

    static void scopedValueExample() {

        WorkshopPrinter.subtitle("ScopedValue - safe contextual binding");

        ScopedValue.where(USER, "alice").run(() -> {

            WorkshopPrinter.print("handleRequest USER", USER.get());

            serviceScopedValue();

            WorkshopPrinter.print("handleRequest after service USER", USER.get());
        });
    }

    static void serviceScopedValue() {

        WorkshopPrinter.print("service sees USER", USER.get());

        alternativeScopedBinding();

        WorkshopPrinter.print("service after nested scope USER", USER.get());
    }

    static void alternativeScopedBinding() {

        WorkshopPrinter.print("creating nested scope  !!!!!!!!!!!!!!!!!`", "USER = mallory");

        ScopedValue.where(USER, "mallory").run(() -> {
            WorkshopPrinter.print("inside nested scope USER !!!!!!!!!!!!!!!!", USER.get());
        });

        WorkshopPrinter.print("nested scope", "finished");
    }
}
