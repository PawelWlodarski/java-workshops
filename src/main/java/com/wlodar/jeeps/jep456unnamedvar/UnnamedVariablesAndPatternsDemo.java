package com.wlodar.jeeps.jep456unnamedvar;

import com.wlodar.WorkshopPrinter;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

public class UnnamedVariablesAndPatternsDemo {

    record Point(int x, int y) {
    }

    static void main(String[] args) {
        WorkshopPrinter.title("Unnamed variables and patterns");
        // can reuse '_' multiple times in the same scope.
        int _ = justWantThisForSideEffects(); // just for side effect
        int _ = 123; // still fine: '_' is "throwaway"

        // Unnamed exception parameter in catch
        tryCatchExample();

        //ignore elements just to dequeue
        demoQueueTriplesToPoints();

        // 3) Unnamed lambda parameters
        //    Great for event handlers, streams, callbacks where you only use some args.
        mapExample();
        demonstrateCallback();

        // 4) Unnamed patterns (record patterns) — ignore components you don't use
        patternMatchingExample();
    }

    private static void tryCatchExample() {
        var s=Math.random() < 0.005 ? "1" : "text";
        try {
            Integer.parseInt(s);
            //Still, if you are ignoring an exception, I'd consider it an anti-pattern
        } catch (NumberFormatException _) {
            WorkshopPrinter.print("2","NumberFormatException caught but exception ignored");
        }
    }

    private static int justWantThisForSideEffects() {
        WorkshopPrinter.print("1","~~~~Executing just for side effect~~~~");
        return 999;
    }

    private static void demoQueueTriplesToPoints() {
        // x1, y1, z1, x2, y2, z2, ...
        Queue<Integer> q = new ArrayDeque<>();
        q.add(10);
        q.add(20);
        q.add(999);

        q.add(30);
        q.add(40);
        q.add(888);

        q.add(50);
        q.add(60);
        q.add(777);

        while (q.size() >= 3) {
            var x = q.remove();
            var y = q.remove();
            var _ = q.remove(); // Unnamed variable: deliberately ignore z

            var p = new Point(x, y);
            System.out.println(p);
        }
    }

    private static void mapExample() {
        var scores = Map.of(
                "alice", 42,
                "bob", 17,
                "carol", 99
        );

        // We ignore the username and only print the score
        WorkshopPrinter.subtitle("Printing scores only (Map example)");
        scores.forEach((_, score) ->
                System.out.println("Score = " + score)
        );
    }

    private static void demonstrateCallback() {
        WorkshopPrinter.subtitle("Demonstrate callback pattern");
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() ->
                processWithCallback((_, result) ->
                        WorkshopPrinter.printfThread("Processing result in callback: %s", result)
                )
        );

        executor.shutdown();
    }

    private static void processWithCallback(BiConsumer<Throwable, String> callback) {
        // Simulate successful processing
        callback.accept(null, "OK");
    }

    private static void patternMatchingExample() {
      WorkshopPrinter.subtitle("Pattern matching example");
        Object obj = new Point(7, 42);

        if (obj instanceof Point(int x, int _)) {
            System.out.println("Matched Point; x = " + x + " (y ignored)");
        }

        // 5) Unnamed patterns in switch (simple, workshop-friendly)
        String description = switch (obj) {
            case Point(int x, int _) -> "Point with x=" + x;
            default -> "Something else";
        };

        System.out.println(description);
    }
}
