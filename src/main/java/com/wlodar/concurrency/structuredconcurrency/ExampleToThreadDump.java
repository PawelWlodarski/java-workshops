package com.wlodar.concurrency.structuredconcurrency;

import com.wlodar.WorkshopPrinter;

import java.util.concurrent.StructuredTaskScope;

public class ExampleToThreadDump {

    static void main() throws InterruptedException {
        WorkshopPrinter.title("Structured Concurrency Thread Dump Example");
        WorkshopPrinter.print("PID: " + ProcessHandle.current().pid());

        try (var outerScope = StructuredTaskScope.open()) {

            var parent = outerScope.fork(() -> {
                Thread.currentThread().setName("parent-virtual-thread");

                WorkshopPrinter.printThread("parent started");

                try (var innerScope = StructuredTaskScope.open()) {

                    var child1 = innerScope.fork(() -> {
                        Thread.currentThread().setName("child-1-virtual-thread");
                        runForever("child-1");
                        return 1;
                    });

                    var child2 = innerScope.fork(() -> {
                        Thread.currentThread().setName("child-2-virtual-thread");
                        runForever("child-2");
                        return 1;
                    });

                    innerScope.join();

                    return child1.get() + child2.get();
                }
            });

            WorkshopPrinter.print("Run this command:");
            WorkshopPrinter.print("jcmd " + ProcessHandle.current().pid()
                    + " Thread.dump_to_file -format=json /tmp/structured-dump.json");

            outerScope.join();

            WorkshopPrinter.print("Parent result: " + parent.get());
        }
    }

    private static void runForever(String name) throws InterruptedException {
        int counter = 0;

        while (true) {
            WorkshopPrinter.printThread(name + " still running: " + counter++);
            Thread.sleep(2_000);
        }
    }
}