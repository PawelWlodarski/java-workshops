package com.wlodar.jeeps.jep499structuredConcurrency;

import com.wlodar.WorkshopPrinter;


/*
 * JEP 499 – Structured Concurrency (Preview, JDK 23/24)
 *
 * This version used StructuredTaskScope.ShutdownOnFailure
 * and explicit throwIfFailed().
 *
 * In JDK 25 (JEP 505), the API was finalized and significantly redesigned.
 * This example is kept for historical and educational purposes.
 */
public class NestedStructuredShutdownDemo {

    public static void main(String[] args) {
        WorkshopPrinter.print("""
            Nested structured preview JEP 499 was deprecated in JDK25
        """);
//        WorkshopPrinter.printfThread("🔧 Starting top-level scope", "");
//
//        try (var outerScope = new StructuredTaskScope.ShutdownOnFailure("outer", Thread.ofVirtual().name("outer-", 1).factory())) {
//
//            outerScope.fork(() -> runNestedScope());
//
//            outerScope.join();
//            outerScope.throwIfFailed();
//
//        } catch (Exception e) {
//            WorkshopPrinter.printfThread("💥 Outer scope failed: %s", e.getMessage());
//        }
//
//        WorkshopPrinter.printfThread("✅ Done", "");
    }

//    static String runNestedScope() {
//        WorkshopPrinter.printfThread("🧵 Entered nested scope", "");
//
//        try (var innerScope = new StructuredTaskScope.ShutdownOnFailure("inner", Thread.ofVirtual().name("inner-", 1).factory())) {
//
//            innerScope.fork(() -> loopWithPrint("nested-task-1"));
//            innerScope.fork(() -> loopWithPrint("nested-task-2"));
//
//            Thread.sleep(300); // Let them run a bit
//            WorkshopPrinter.printfThread("🛑 Calling shutdown on nested scope", "");
//            innerScope.shutdown(); // Interrupt both subtasks
//
//            innerScope.join();
//            innerScope.throwIfFailed();
//
//        } catch (Exception e) {
//            throw new RuntimeException("Some domain Exception",e);
//        }
//
//        return "nested-scope-finished";
//    }
//
//    static String loopWithPrint(String name) {
//        try {
//            while (true) {
//                Thread.sleep(100);
//                WorkshopPrinter.printfThread("%s is running...", name);
//            }
//        } catch (InterruptedException e) { //COMMENT THIS CATCH TO SEE DANGER!!!
//            WorkshopPrinter.printfThread("⚠️ %s was interrupted", name);
//            return "cancelled";
//        } catch (Exception e) {  //If a subtask completes after shutdown, then the subtask’s result or exception is ignored.
//            WorkshopPrinter.printfThread("CATCHING ALL EXCEPTIONS IN SUBTASK!!", name);
//            throw new RuntimeException("this is some operational exception",e);
//        }
//    }
}

