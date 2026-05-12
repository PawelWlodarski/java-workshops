package com.wlodar.concurrency.structuredconcurrency;

import com.wlodar.WorkshopPrinter;
import com.wlodar.concurrency.JustSleep;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConcurrencyProblems {


    static void main() throws ExecutionException, InterruptedException {
        happyPath();
//        problem1(); // failing propagation
//        problem2(); //scope value propagation
    }

    private static void happyPath() throws ExecutionException, InterruptedException {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            var f1 = executor.submit(() -> {
                JustSleep.exactly(200);
                return 1;
            });

            var f2 = executor.submit(() -> {
                JustSleep.exactly(100);
                return 2;
            });

            var result = f1.get() + f2.get();
            WorkshopPrinter.print("Result: " + result);

        }
    }

    //even if one failed second one still is working for result which will be ignored

    private static void problem1() throws ExecutionException, InterruptedException {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            Future<Integer> f1 = executor.submit(() -> {
                WorkshopPrinter.print("f1 working");
                JustSleep.exactly(200);
                WorkshopPrinter.print("f1 failing");
                throw new RuntimeException("f1 failed");
            });

            var f2 = executor.submit(() -> {
                WorkshopPrinter.print("f2 working");
                JustSleep.exactly(100);
                for (int i = 0; i < 10; i++) {
                    WorkshopPrinter.print("f2 still working");
                    JustSleep.exactly(1000);
                }

                return 2;
            });

            var result = f1.get() + f2.get();
            WorkshopPrinter.print("Result: " + result);

        }
    }

    private static ScopedValue<Integer> resultScope = ScopedValue.newInstance();

    private static void problem2() throws ExecutionException, InterruptedException {

        var executor=Executors.newVirtualThreadPerTaskExecutor();

        Callable<Integer> parent = ()-> ScopedValue.where(resultScope, 1).call(() -> {
            var t1=executor.submit(()-> resultScope.get()+1);
            var t2=executor.submit(()-> resultScope.get()+2);
            return t1.get()+t2.get();
        });

        var resultFuture=executor.submit(parent);

        WorkshopPrinter.print("result : "+resultFuture.get());

        executor.close();
    }

}
