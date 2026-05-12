package com.wlodar.concurrency.virtualthreads;

import com.wlodar.WorkshopPrinter;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class VirtualThreadsIntroDemo {

    private static final int REQUEST_COUNT = 200;
    private static final int PLATFORM_THREAD_POOL_SIZE = 20;
    private static final int DATABASE_LATENCY_MS = 200;

    static void main() throws Exception {
        WorkshopPrinter.title("Virtual Threads - Intro Demo");

        WorkshopPrinter.print("Scenario", "Many incoming requests, each waiting for a fake database");
        WorkshopPrinter.print("Requests", REQUEST_COUNT);
        WorkshopPrinter.print("Database latency per request [ms]", DATABASE_LATENCY_MS);
        WorkshopPrinter.print("Platform pool size", PLATFORM_THREAD_POOL_SIZE);

//        runSimulationWithPlatformThreads();
        runSimulationWithVirtualThreads();

    }

    private static void runSimulationWithPlatformThreads() throws Exception {
        WorkshopPrinter.subtitle("Simulation with platform threads");

        var server = new FakeServer(new FakeDatabase(DATABASE_LATENCY_MS));
        var startedAt = Instant.now();

        try (var executor = Executors.newFixedThreadPool(PLATFORM_THREAD_POOL_SIZE)) {
            List<Future<String>> futures = new ArrayList<>();

            for (int requestId = 1; requestId <= REQUEST_COUNT; requestId++) {
                int id = requestId;
                futures.add(executor.submit(() -> server.handleRequest(id)));
            }

            for (Future<String> future : futures) {
                future.get();
            }
        }

        var duration = Duration.between(startedAt, Instant.now());

        WorkshopPrinter.print("Platform threads total time [ms]", duration.toMillis());
        WorkshopPrinter.print("Expected effect",
                "Requests are processed in batches because the pool is limited");
    }

    private static Callable<String> createRequestTask(FakeServer server, int requestId) {
        return () -> server.handleRequest(requestId);
    }

    private static void runSimulationWithVirtualThreads() throws Exception {
        WorkshopPrinter.subtitle("Simulation with virtual threads");

        var server = new FakeServer(new FakeDatabase(DATABASE_LATENCY_MS));
        var startedAt = Instant.now();

        //important to have virtual thread name displayed
        var factory = Thread.ofVirtual().name("request-vt-", 0).factory();

        try (var executor = Executors.newThreadPerTaskExecutor(factory)) {
            List<Future<String>> futures = IntStream.rangeClosed(1, REQUEST_COUNT)
                    .mapToObj(id ->createRequestTask(server, id))
                    .map(executor::submit)
                    .toList();

            for (Future<String> future : futures) {
                future.get();
            }
        }

        var duration = Duration.between(startedAt, Instant.now());

        WorkshopPrinter.print("Virtual threads total time [ms]", duration.toMillis());
        WorkshopPrinter.print("Expected effect",
                "Many waiting requests can progress concurrently without a large platform-thread pool");
    }

    static class FakeServer {
        private final FakeDatabase fakeDatabase;

        FakeServer(FakeDatabase fakeDatabase) {
            this.fakeDatabase = fakeDatabase;
        }

        String handleRequest(int requestId) throws InterruptedException {
            WorkshopPrinter.printfThread("Handling request %s", String.valueOf(requestId));

            String result = fakeDatabase.loadUserData(requestId);

            WorkshopPrinter.printfThread("Finished request %s", String.valueOf(requestId));
            return result;
        }
    }

    static class FakeDatabase {
        private final int latencyMs;

        FakeDatabase(int latencyMs) {
            this.latencyMs = latencyMs;
        }

        String loadUserData(int requestId) throws InterruptedException {
            WorkshopPrinter.printfThread("Database call for request %s", String.valueOf(requestId));
            Thread.sleep(latencyMs);
            return "user-data-" + requestId;
        }
    }
}