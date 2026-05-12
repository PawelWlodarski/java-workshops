package com.wlodar.concurrency.virtualthreads;

import com.wlodar.WorkshopPrinter;

public class SimpleVirtualThread {
    static void main() throws InterruptedException{
        justRunVirtualThread();
//        runAndSleep();
//        virtualThreadAndCpu();
        
    }

    private static void justRunVirtualThread() throws InterruptedException {
        WorkshopPrinter.title("Simple Virtual Thread");

        Thread.Builder.OfVirtual builder = Thread.ofVirtual().name("My First Virtual Thread");
//        Thread.Builder.OfPlatform builder = Thread.ofPlatform().name("Not My First Platform Thread");
        Thread thread = builder.start(() -> {
            WorkshopPrinter.subtitle("Virtual thread started");
            WorkshopPrinter.printThread("Virtual thread running");
        });

        thread.join();

    }

    private static void runAndSleep() throws InterruptedException {
        WorkshopPrinter.title("20 Threads with Sleep");

        var threads = new java.util.ArrayList<Thread>();
        var numberOfThreads=2000;
        var maxSleepSeconds=50;
        
        long startNanos = System.nanoTime();

        for (int threadNumber = 1; threadNumber <= numberOfThreads; threadNumber++) {
//            Thread thread = Thread.ofPlatform().name("PlatformThread-" + threadNumber).start(() -> {
        Thread thread = Thread.ofVirtual().name("VirtualThread-" + threadNumber).start(() -> {
                try {
                    int sleepSeconds = 1 + (int) (Math.random() * maxSleepSeconds);
                    WorkshopPrinter.printfThread("Starting, will sleep for %s seconds", String.valueOf(sleepSeconds));

                    Thread.sleep(sleepSeconds * 1000L);

                    WorkshopPrinter.printfThread("Finished sleeping for %s seconds", String.valueOf(sleepSeconds));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.join();
        }

        long elapsedMillis = (System.nanoTime() - startNanos) / 1_000_000;

        WorkshopPrinter.print("All platform threads completed in " + elapsedMillis + " ms");
    }

    private static volatile long blackhole;

    private static void virtualThreadAndCpu() throws InterruptedException {
        Thread.Builder.OfVirtual builder = Thread.ofVirtual().name("Run On Cpu");


        Runnable runnable = () -> {
            var counter=0;
            while (true) {
                WorkshopPrinter.printfThread("Virtual Thread %s", String.valueOf(counter));
                counter++;
                blackhole = counter;
            }
        };

        var t=builder.start(runnable);
        t.join();

    }
}
