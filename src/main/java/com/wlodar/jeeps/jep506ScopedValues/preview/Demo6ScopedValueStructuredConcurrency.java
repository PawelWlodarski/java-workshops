package com.wlodar.jeeps.jep506ScopedValues.preview;

import com.wlodar.WorkshopPrinter;

import java.util.concurrent.StructuredTaskScope;

public class Demo6ScopedValueStructuredConcurrency {

    // Context value that will be shared across the whole execution scope
    // and automatically visible to child tasks
    private static final ScopedValue<String> REQUEST_ID = ScopedValue.newInstance();

    static void main() {

        WorkshopPrinter.title("ScopedValue + Structured Concurrency");

        // Bind contextual value for the whole execution scope
        ScopedValue.where(REQUEST_ID, "req-777").run(() -> {

            WorkshopPrinter.subtitle("Parent scope");
            WorkshopPrinter.print("REQUEST_ID", REQUEST_ID.get());

            /*
             * Open a structured concurrency scope.
             *
             * Key idea:
             * All tasks created inside this scope are "children" of this execution.
             * ScopedValue bindings are automatically inherited by those tasks.
             *
             * No ThreadLocal.
             * No parameter passing.
             * No manual propagation.
             */
            try (var scope = StructuredTaskScope.<String>open()) {

                /*
                 * Fork two concurrent tasks.
                 *
                 * Each task runs independently (usually on virtual threads)
                 * but still sees the same ScopedValue binding.
                 */
                var userTask = scope.fork(Demo6ScopedValueStructuredConcurrency::loadUser);
                var orderTask = scope.fork(Demo6ScopedValueStructuredConcurrency::loadOrders);

                /*
                 * Wait until all child tasks finish.
                 */
                scope.join();

                /*
                 * Retrieve results from subtasks.
                 */
                WorkshopPrinter.subtitle("Results");
                WorkshopPrinter.print("userTask", userTask.get());
                WorkshopPrinter.print("orderTask", orderTask.get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        });
    }

    static String loadUser() {

        /*
         * Notice:
         * This method receives NO parameters,
         * but still has access to REQUEST_ID.
         *
         * The ScopedValue binding automatically flows
         * from the parent scope into this child task.
         */
        WorkshopPrinter.printThread("loadUser sees REQUEST_ID = " + REQUEST_ID.get());

        sleep(200);

        return "Alice";
    }

    static String loadOrders() {

        /*
         * Same here: independent concurrent task
         * but still sees the same contextual value.
         */
        WorkshopPrinter.printThread("loadOrders sees REQUEST_ID = " + REQUEST_ID.get());

        sleep(300);

        return "3 orders";
    }

    static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
