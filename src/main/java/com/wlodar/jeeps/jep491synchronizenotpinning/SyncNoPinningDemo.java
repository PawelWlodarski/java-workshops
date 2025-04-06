package com.wlodar.jeeps.jep491synchronizenotpinning;

import com.wlodar.WorkshopPrinter;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SyncNoPinningDemo {

    public static void main(String[] args) throws InterruptedException {
        WorkshopPrinter.title("pinning demo");

        int cores = Runtime.getRuntime().availableProcessors();
        WorkshopPrinter.print("cores: ", cores);
        int virtualThreads = 100*cores;

        // Create a virtual thread executor bound to ONE platform thread
        ExecutorService executor = Executors.newThreadPerTaskExecutor(
                Thread.ofVirtual().name("vthread-", 0).factory()
        );

        CountDownLatch latch = new CountDownLatch(virtualThreads);
        Instant start = Instant.now();

        for (int i = 0; i < virtualThreads; i++) {
            executor.execute(() -> {
                Object LOCK = new Object();
                synchronized (LOCK) {
                    try {
                        WorkshopPrinter.printThread("before critical section");
                        Thread.sleep(100); // Critical section
                        WorkshopPrinter.printThread("after critical section");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        Duration time = Duration.between(start, Instant.now());
        System.out.println("✅ Finished " + virtualThreads + " synchronized virtual threads in " + time.toMillis() + " ms");
    }
}
