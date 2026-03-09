package com.wlodar.jeeps.jep499structuredConcurrency;

import com.wlodar.WorkshopPrinter;


/*
 * JEP 499 – Structured Concurrency (4th Preview, JDK 23/24)
 *
 * This example demonstrates behavioral rules from the preview API:
 *
 * 1) A StructuredTaskScope was thread-confined:
 *    - Subtasks could only be forked by the thread that created the scope.
 *    - Attempting to fork from another thread resulted in an exception.
 *
 * 2) A subtask could cancel its own scope via shutdown().
 *
 * In JDK 25 (JEP 505), Structured Concurrency was finalized and the API
 * was significantly redesigned:
 *  - ShutdownOnFailure was removed
 *  - throwIfFailed() was removed
 *  - Constructors were replaced with StructuredTaskScope.open(...)
 *  - Cancellation semantics are now driven by joiners and configuration
 *
 * This code is preserved for historical and educational purposes to
 * illustrate how the preview API behaved before finalization.
 */
public class ScopeSharedAcrossThreadsDemo {

    public static void main(String[] args) throws InterruptedException {
        WorkshopPrinter.print("""
            Nested structured preview JEP 499 was deprecated in JDK25
        """);



//        WorkshopPrinter.print("""
//            Nested structured preview JEP 499 was deprecated in JDK25
//        """);
//
//        WorkshopPrinter.printfThread("🔧 Main: Creating scope in main thread", "");
//
//        youCantCreateSubtaskFromOtherThread();
//        //butYouCanCancellScopeFromSubtask();

    }

//    private static void youCantCreateSubtaskFromOtherThread() {
//        ThreadFactory factory = Thread.ofVirtual().name("shared-scope-vt-", 1).factory();
//
//        try (var scope = new StructuredTaskScope.ShutdownOnFailure("shared-scope", factory)) {
//
//            // 👇 Pass the scope to another thread to fork a task
//            Thread helperThread = Thread.ofVirtual().start(() -> {
//                WorkshopPrinter.printfThread("🔁 Helper thread: forking task into shared scope", "");
//                //scope.shutdown();
//                scope.fork(() -> {
//                    WorkshopPrinter.printfThread("🎯 Task running in shared scope", "");
//                    Thread.sleep(500);
//                    return "Hello from shared scope!";
//                });
//            });
//
//            helperThread.join(); // Wait for fork to be submitted
//
//            scope.join();
//            scope.throwIfFailed();
//
//        } catch (Exception e) {
//            WorkshopPrinter.printfThread("💥 Caught exception: %s", e.getMessage());
//        }
//
//        WorkshopPrinter.printfThread("✅ Done", "");
//    }
//
//    private static void butYouCanCancellScopeFromSubtask(){
//        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
//
//            scope.fork(() -> {
//                Thread.sleep(200);
//                WorkshopPrinter.printfThread("🔕 Subtask wants to cancel the scope", "");
//                scope.shutdown(); // Cancel from inside
//                return "cancelled-from-subtask";
//            });
//
//            scope.fork(() -> {
//                while (true) {
//                    Thread.sleep(100);
//                    WorkshopPrinter.printfThread("🔁 Still running...", "");
//                }
//            });
//
//            scope.join();
//            scope.throwIfFailed();
//
//        } catch (Exception e) {
//            WorkshopPrinter.printfThread("💥 Exception: %s", e.getMessage());
//        }
//    }
}

