package com.wlodar.jeeps.jep487scopedvalue;

import com.wlodar.WorkshopPrinter;

public class ScopedValueDemoSimpleExample {

    // Define a scoped value
    static final ScopedValue<String> requestId = ScopedValue.newInstance();

    public static void main(String[] args) {
        WorkshopPrinter.title("Scoped Value Simple Example");
        // Bind the value for a scoped block
        ScopedValue.where(requestId, "REQ-1234").run(ScopedValueDemoSimpleExample::processRequest);

        //UNCOMMENT and check
        //WorkshopPrinter.print("forbidden",requestId.get());

    }

    static void processRequest() {
        // Access the value without passing it explicitly
        WorkshopPrinter.print("Handling request ID: ", requestId.get());
        ScopedValue.where(requestId,"new-ID").run(ScopedValueDemoSimpleExample::callService);

        //uncomment to see fail in virtual thread
        //ScopedValue.where(requestId,"new-ID").run(ScopedValueDemoSimpleExample::callServiceInVirtualThread);

    }

    static void callService() {
            String threadName = Thread.currentThread().getName();
            System.out.printf("[%s] Virtual thread sees: %s%n", threadName, requestId.get());
            WorkshopPrinter.print("Inside service with ID: " , requestId.get());
    }

    static void callServiceInVirtualThread() {
        try {
            Thread.startVirtualThread(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.printf("[%s] Virtual thread sees: %s%n", threadName, requestId.get());
                WorkshopPrinter.print("Inside service with ID: " , requestId.get());
            }).join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
