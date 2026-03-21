package com.wlodar.jeeps.jep444virtualthreads;

import com.wlodar.WorkshopPrinter;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Jep444VirtualThreadsDemo {

    private static final int TASKS = 10_000;

    static void main() throws Exception {
        WorkshopPrinter.title("JEP 444: Virtual Threads");

        runVirtualThreadsDemo();
    }

    static void runVirtualThreadsDemo() throws Exception {
        WorkshopPrinter.subtitle("10_000 cheap virtual threads");

        var started = new AtomicInteger();
        var finished = new AtomicInteger();

        Instant begin = Instant.now();

        //new executor
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = IntStream.range(0, TASKS)
                    .mapToObj(i -> executor.submit(() -> {
                        int currentStarted = started.incrementAndGet();

                        if (i < 5) {
                            WorkshopPrinter.printThread("Started task " + i + " (started so far: " + currentStarted + ")");
                        }

                        Thread.sleep(Duration.ofSeconds(1));

                        int currentFinished = finished.incrementAndGet();

                        if (i < 5) {
                            WorkshopPrinter.printThread("Finished task " + i + " (finished so far: " + currentFinished + ")");
                        }

                        return i;
                    }))
                    .toList();

            for (var future : futures) {
                future.get();
            }
        }

        long millis = Duration.between(begin, Instant.now()).toMillis();

        WorkshopPrinter.print("Tasks", TASKS);
        WorkshopPrinter.print("Finished", finished.get());
        WorkshopPrinter.print("Total time", millis + " ms");
        WorkshopPrinter.print("Conclusion", "10_000 blocking tasks can run concurrently because virtual threads are cheap");
    }
}
