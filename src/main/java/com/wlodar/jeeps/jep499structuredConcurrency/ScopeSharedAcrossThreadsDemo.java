package com.wlodar.jeeps.jep499structuredConcurrency;

import com.wlodar.WorkshopPrinter;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.ThreadFactory;

public class ScopeSharedAcrossThreadsDemo {

    public static void main(String[] args) throws InterruptedException {
        WorkshopPrinter.printfThread("🔧 Main: Creating scope in main thread", "");

        youCantCreateSubtaskFromOtherThread();
        //butYouCanCancellScopeFromSubtask();

    }

    private static void youCantCreateSubtaskFromOtherThread() {
        ThreadFactory factory = Thread.ofVirtual().name("shared-scope-vt-", 1).factory();

        try (var scope = new StructuredTaskScope.ShutdownOnFailure("shared-scope", factory)) {

            // 👇 Pass the scope to another thread to fork a task
            Thread helperThread = Thread.ofVirtual().start(() -> {
                WorkshopPrinter.printfThread("🔁 Helper thread: forking task into shared scope", "");
                //scope.shutdown();
                scope.fork(() -> {
                    WorkshopPrinter.printfThread("🎯 Task running in shared scope", "");
                    Thread.sleep(500);
                    return "Hello from shared scope!";
                });
            });

            helperThread.join(); // Wait for fork to be submitted

            scope.join();
            scope.throwIfFailed();

        } catch (Exception e) {
            WorkshopPrinter.printfThread("💥 Caught exception: %s", e.getMessage());
        }

        WorkshopPrinter.printfThread("✅ Done", "");
    }

    private static void butYouCanCancellScopeFromSubtask(){
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            scope.fork(() -> {
                Thread.sleep(200);
                WorkshopPrinter.printfThread("🔕 Subtask wants to cancel the scope", "");
                scope.shutdown(); // Cancel from inside
                return "cancelled-from-subtask";
            });

            scope.fork(() -> {
                while (true) {
                    Thread.sleep(100);
                    WorkshopPrinter.printfThread("🔁 Still running...", "");
                }
            });

            scope.join();
            scope.throwIfFailed();

        } catch (Exception e) {
            WorkshopPrinter.printfThread("💥 Exception: %s", e.getMessage());
        }
    }
}

