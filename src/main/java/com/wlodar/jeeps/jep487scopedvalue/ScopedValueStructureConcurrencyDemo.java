package com.wlodar.jeeps.jep487scopedvalue;

import com.wlodar.WorkshopPrinter;

import java.util.concurrent.StructuredTaskScope;

public class ScopedValueStructureConcurrencyDemo {

    static final ScopedValue<String> requestId = ScopedValue.newInstance();

    public static void main(String[] args) {
        WorkshopPrinter.title("Scoped Value Simple Example");

        // Bind the value inside ScopedValue.where()
        ScopedValue.where(requestId, "REQ-1234").run(() -> {
            messageWithId("main");
            try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
                scope.fork(() -> processRequest()); // Virtual thread runs inside the structured scope
                scope.join(); // Wait for completion
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

    }

    static Void processRequest() {
        messageWithId("processRequest");

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            scope.fork(() -> callService());
            scope.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    static Void callService() {
        messageWithId("callService");
        return null;
    }

    private static void messageWithId(String message) {
        long threadId = Thread.currentThread().threadId();
        System.out.printf("[%s] %s thread sees: %s%n", threadId, message ,requestId.get());
    }


}
