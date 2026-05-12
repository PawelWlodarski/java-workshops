package com.wlodar.concurrency.structuredconcurrency;

import com.wlodar.WorkshopPrinter;
import com.wlodar.concurrency.JustSleep;

import java.util.concurrent.Callable;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;

public class StructuredScopeBasics {


    static void main() throws InterruptedException {
//        simpleAddition();
        //explain why interrupted exception finally is useful
//        failingTask();
//        sharingScopeNaiveApproach();
//        sharingScopeWorking();
        sharingScopeWorkingRefactored();
    }

    private static void simpleAddition() throws InterruptedException {
        try(var scope= StructuredTaskScope.open()) {
            var t1=scope.fork(()-> {
                Thread.sleep(100);
                return 1;
            });

            var t2=scope.fork(()-> {
                for (int i = 0; i < 3; i++) {
                    WorkshopPrinter.print("f2 still working");
                    Thread.sleep(400);
                }
                return 2;
            });

            scope.join();

            WorkshopPrinter.print("Result: "+(t1.get()+t2.get()));

        }
    }

    private static void failingTask() throws InterruptedException {
        try(var scope= StructuredTaskScope.open()) {
            Subtask<Integer> t1=scope.fork(()-> {
                WorkshopPrinter.print("f1 working");
                Thread.sleep(200);
                WorkshopPrinter.print("f1 failing");
                throw new RuntimeException("f1 failed");
            });

            var t2=scope.fork(()-> {
                WorkshopPrinter.print("f2 working");
                Thread.sleep(100);
                for (int i = 0; i < 10; i++) {
                    WorkshopPrinter.print("f2 still working");
                    JustSleep.exactly(1000);
//                    Thread.sleep(1000);
                }

                return 2;
            });

            scope.join();

            WorkshopPrinter.print("Result: "+(t1.get()+t2.get()));

        }
    }

    private static ScopedValue<Integer> resultScope = ScopedValue.newInstance();

    private static void sharingScopeNaiveApproach() throws InterruptedException {


        try(var scope= StructuredTaskScope.open()) {
            Callable<Integer> parent = ()-> {
                var t1=scope.fork(()-> resultScope.get()+1);
                var t2=scope.fork(()-> resultScope.get()+2);

                return t1.get()+t2.get();
            };

            var result=scope.fork(parent);

            scope.join();

            WorkshopPrinter.print("Result: "+result.get());
        }
    }

    //Scoped Value has to be FIRST!!!! then internal scope!!!
    private static void sharingScopeWorking() throws InterruptedException {

        Callable<Integer> parent = ()-> ScopedValue.where(resultScope, 1).call(()-> {
            try(var innerScope=StructuredTaskScope.open()){
                var t1=innerScope.fork(()-> resultScope.get()+1);
                var t2=innerScope.fork(()-> resultScope.get()+2);

                innerScope.join(); //no special example for this one but you still need to join on inner scope

                return t1.get()+t2.get();
            }
        });

        try(var scope= StructuredTaskScope.open()) {
            var result=scope.fork(parent);
            scope.join();
            WorkshopPrinter.print("Result: "+result.get());
        }
    }


    private static void sharingScopeWorkingRefactored() throws InterruptedException {
        Callable<Integer> parent = ()-> ScopedValue.where(resultScope, 1).call(StructuredScopeBasics::innerScopeOperations);

        try(var scope= StructuredTaskScope.open()) {
            var result=scope.fork(parent);
            scope.join();
            WorkshopPrinter.print("Result: "+result.get());
        }
    }

    private static Integer innerScopeOperations() throws InterruptedException {
        try(var innerScope=StructuredTaskScope.open()){
            var t1=innerScope.fork(()-> resultScope.get()+1);
            var t2=innerScope.fork(()-> resultScope.get()+2);

            innerScope.join(); //no special example for this one but you still need to join on inner scope

            return t1.get()+t2.get();
        }
    }

}
