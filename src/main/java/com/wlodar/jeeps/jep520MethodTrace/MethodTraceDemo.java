package com.wlodar.jeeps.jep520MethodTrace;

import com.wlodar.WorkshopPrinter;

public class MethodTraceDemo {

    static void main() throws Exception {
        WorkshopPrinter.title("JEP 520 - JFR Method Timing & Tracing");

        var service = new BufferService();

        WorkshopPrinter.print("Section", "Warmup");
        service.warmupCache();

        Thread.sleep(150);

        WorkshopPrinter.print("Section", "Import flow");
        for (int i = 0; i < 3; i++) {
            service.importData(500 + i * 100);
        }

        Thread.sleep(150);

        WorkshopPrinter.print("Section", "Traffic burst flow");
        for (int i = 0; i < 4; i++) {
            service.handleBurstTraffic(1000 + i * 200);
        }

        Thread.sleep(150);

        WorkshopPrinter.print("Section", "Done");
        WorkshopPrinter.print("Tip", "Now inspect MethodTrace events in the JFR recording");
    }
}