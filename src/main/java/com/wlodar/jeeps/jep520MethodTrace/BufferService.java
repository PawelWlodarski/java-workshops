package com.wlodar.jeeps.jep520MethodTrace;

import com.wlodar.WorkshopPrinter;

public class BufferService {

    private int capacity = 8;

    public void warmupCache() {
        WorkshopPrinter.print("Flow", "warmupCache()");
        ensureCapacity(12);
    }

    public void importData(int records) {
        WorkshopPrinter.print("Flow", "importData(" + records + ")");
        parseInput(records);
    }

    public void handleBurstTraffic(int requests) {
        WorkshopPrinter.print("Flow", "handleBurstTraffic(" + requests + ")");
        acceptRequests(requests);
    }

    private void parseInput(int records) {
        busyWork(8);
        ensureCapacity(records / 40);
    }

    private void acceptRequests(int requests) {
        busyWork(5);
        ensureCapacity(requests / 60);
    }

    private void ensureCapacity(int required) {
        if (required > capacity) {
            resizeBuffer(required);
        }
    }

    private void resizeBuffer(int required) {
        long start = System.nanoTime();

        int oldCapacity = capacity;
        busyWork(20 + required / 20);
        capacity = Math.max(required, capacity * 2);

        long elapsedMicros = (System.nanoTime() - start) / 1_000;
        WorkshopPrinter.print(
                "resizeBuffer",
                "old=" + oldCapacity + ", new=" + capacity + ", required=" + required + ", took≈" + elapsedMicros + "us"
        );
    }

    private static void busyWork(int millis) {
        long end = System.nanoTime() + millis * 1_000_000L;
        double x = 0;
        while (System.nanoTime() < end) {
            x += Math.sqrt(123.456);
        }
        if (x == 42) {
            System.out.println(x);
        }
    }
}