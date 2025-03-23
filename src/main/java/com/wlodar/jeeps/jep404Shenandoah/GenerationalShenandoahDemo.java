package com.wlodar.jeeps.jep404Shenandoah;

import com.wlodar.WorkshopPrinter;

public class GenerationalShenandoahDemo {
    public static void main(String[] args) throws InterruptedException {
        WorkshopPrinter.title("GenerationalShenandoah");

        byte[] longLived = new byte[100 * 1024 * 1024]; // 100MB - long-lived

        while (true) {
            WorkshopPrinter.print("Performing Allocation");
            for (int i = 0; i < 1000; i++) {
                // short-lived objects
                byte[] garbage = new byte[1_000_000]; // 1MB
            }

            Thread.sleep(100);// Allow time for GC

        }
    }
}
