package com.wlodar.concurrency.scopedvalues;

import com.wlodar.WorkshopPrinter;
import com.wlodar.concurrency.JustSleep;

import java.util.function.IntFunction;

import static com.wlodar.WorkshopPrinter.*;

public class SimpleScopedValueForVirtualThread {

    private static final ScopedValue<String> REQUEST_ID = ScopedValue.newInstance();

    static void main() {
        //failedAttempt1();
        successfulAttempt2();

    }


    //binding has to happen inside thread
    private static void failedAttempt1() {
        title("Simple ScopedValue with Virtual Thread");

        printRequestId();

        Runnable task = () -> WorkshopPrinter.printThread("Inside task : "+REQUEST_ID.get()); //no more Function<int,runnable>

        for (int i = 0; i < 5; i++) {
            ScopedValue.where(REQUEST_ID, "request-" + i).run(() -> {
                        var t = Thread.startVirtualThread(task);
                        join(t);
                    }
            );
        }
    }

    //value is preserved across carrier threads
    private static void successfulAttempt2() {

        IntFunction<Runnable> task = requestId -> () -> ScopedValue.where(REQUEST_ID, "request-" + requestId).run(() -> {
            printRequestId();
            JustSleep.around(1000);
            printRequestId();
            JustSleep.around(1000);
            printRequestId();
        });

        for (int i = 0; i < 5; i++) {
            var t=Thread.startVirtualThread(task.apply(i));
            join(t);
        }

        WorkshopPrinter.print("after all threads finished");
        printRequestId();
    }


    private static void printRequestId() {
        if (REQUEST_ID.isBound()) {
            printThread("REQUEST_ID = " + REQUEST_ID.get());
        } else {
            printThread("REQUEST_ID is not bound");
        }
    }

    private static void join(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }


}
