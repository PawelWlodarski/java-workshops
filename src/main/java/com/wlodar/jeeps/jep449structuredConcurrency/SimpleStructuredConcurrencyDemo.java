package com.wlodar.jeeps.jep449structuredConcurrency;

import com.wlodar.WorkshopPrinter;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.ThreadFactory;

public class SimpleStructuredConcurrencyDemo {

    public static void main(String[] args) throws ExecutionException {
        WorkshopPrinter.title("Structured concurrency demo");

        ThreadFactory namedVirtualThreads = Thread.ofVirtual()
                .name("virtual-", 0)  // names will be: virtual-0, virtual-1, ...
                .factory();

        //show default threadFactory
        try (var scope = new StructuredTaskScope.ShutdownOnFailure("JugScope",namedVirtualThreads)) {
           WorkshopPrinter.print("Starting tasks in scope");
            var hello = scope.fork(() -> delayedResult("Hello", 500));
            var world = scope.fork(() -> delayedResult("World", 300));


            WorkshopPrinter.print("waiting both tasks to complete...");
            scope.join();
            scope.throwIfFailed();

            System.out.println("✅ Combined Result: " + hello.get() +" " + world.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static String delayedResult(String message, int millis) throws InterruptedException {
        Thread.sleep(millis);
        System.out.printf("[%s] Done: %s%n", Thread.currentThread().getName(), message);
        return message;
    }
}
