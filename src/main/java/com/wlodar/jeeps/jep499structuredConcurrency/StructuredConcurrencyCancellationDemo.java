package com.wlodar.jeeps.jep499structuredConcurrency;

import com.wlodar.WorkshopPrinter;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.ThreadFactory;

public class StructuredConcurrencyCancellationDemo {

    public static void main(String[] args) {
        WorkshopPrinter.title("🚦 Starting structured scope with cancellation...");

        // 👷 Custom-named virtual threads: "scoped-vt-0", "scoped-vt-1", ...
        ThreadFactory threadFactory = Thread.ofVirtual()
                .name("scoped-vt-", 1)
                .factory();

        try (var scope = new StructuredTaskScope.ShutdownOnFailure("FailFastScope", threadFactory)) {

            scope.fork(() -> failAfter("❌ Failing task", 800));
            scope.fork(() -> loopUntilCancelled("🔁 Looping task"));

            scope.join();             // Wait for both to finish (or one to fail)
            scope.throwIfFailed();    // Propagate any exceptions

        } catch (Exception e) {
            System.out.println("💥 Caught exception: " + e.getMessage());
        }
    }

    static String failAfter(String name, int delayMs) throws InterruptedException {
        WorkshopPrinter.printfThread("%s started.",name);
        Thread.sleep(delayMs);
        WorkshopPrinter.printfThread("%s is throwing an exception now.", name);
        throw new RuntimeException("Intentional failure in task: " + name);
    }

    static String loopUntilCancelled(String name){
        while (true) {
            //similar to kotlin cancellation is cooperative but in
            // java interruption is better handled/has better support
            //than kotlin "isCancelled" flag. Still during loop operation current thread status has to be checked
            //https://kotlinlang.org/docs/cancellation-and-timeouts.html#cancellation-is-cooperative
            if (Thread.currentThread().isInterrupted()) {
                WorkshopPrinter.printfThread("%s was interrupted and is exiting.", name);
                break;
            }
            WorkshopPrinter.printfThread("%s is running...",name);
            sleepAndCatch(name);
        }
        WorkshopPrinter.printfThread("%s is cancelled...",name);
        return "cancelled";
    }

    private static void sleepAndCatch(String name) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            //Check what happen if you catch interrupted exception!!!
            Thread.currentThread().interrupt(); // Important: preserve interrupted status
            System.out.printf("[%s] %s received InterruptedException.%n", Thread.currentThread().getName(), name);
        }
    }
}

