package com.wlodar.jeeps.jep506ScopedValues;

import com.wlodar.WorkshopPrinter;

public class Demo7ScopedValueEscape {
    private static final ScopedValue<String> USER = ScopedValue.newInstance();

    static void main() {

        WorkshopPrinter.title("ScopedValue - escaping the scope");

        /*
         * call(...) is like run(...), but returns a value.
         *
         * Here we use it to:
         * 1. bind USER for a limited execution scope
         * 2. start background work inside that scope
         * 3. return a message from the scoped block
         */
        var result = ScopedValue.where(USER, "alice").call(() -> {

            WorkshopPrinter.subtitle("Inside scope");
            WorkshopPrinter.print("USER", USER.get());

            /*
             * Start background work that will try to read USER later.
             *
             * Important teaching point:
             * this is unstructured work
             * - we do not join it!!!!!!!!!!!!!!!!  <- why structured concurrency is important!!!!!!!
             * - we do not keep it inside a structured scope
             *
             * So this task may outlive the ScopedValue binding.
             */
            Thread.startVirtualThread(() -> {
                sleep(300);

                WorkshopPrinter.subtitle("Background task after scope ended");

                try {
                    WorkshopPrinter.print("USER", USER.get());
                } catch (Exception e) {
                    WorkshopPrinter.print("exception type", e.getClass().getSimpleName());
                    WorkshopPrinter.print("exception message", e.getMessage());
                }
            });

            /*
             * Return something from the scoped block.
             * This demonstrates call(...) instead of run(...).
             */
            return "scoped work finished";
        });

        /*
         * At this point the scope is already finished,
         * so USER is no longer bound in the current thread.
         */
        WorkshopPrinter.subtitle("After call()");
        WorkshopPrinter.print("result", result);
        WorkshopPrinter.print("USER.isBound()", USER.isBound());

        /*
         * Wait long enough so the background virtual thread definitely runs
         * after the ScopedValue scope has ended.
         *
         * This makes the demo deterministic.
         */
        sleep(1000);
    }

    static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
