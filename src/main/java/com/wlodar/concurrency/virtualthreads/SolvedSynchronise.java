package com.wlodar.concurrency.virtualthreads;

import com.wlodar.WorkshopPrinter;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.IntStream;

public class SolvedSynchronise {


    static void main() throws ExecutionException, InterruptedException {
        blockVirtualThreadWithWait();
    }

    private static void blockVirtualThreadWithWait() throws ExecutionException, InterruptedException {
        var factory = Thread.ofVirtual().name("myvirtual-", 0).factory();
        var monitor = new Object();



        Function<Integer,Runnable> r = i -> () -> {
            try {
                WorkshopPrinter.printThread("Virtual thread created with number: " + i);
                synchronized (monitor) {
                    monitor.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };


        try (var executor = Executors.newThreadPerTaskExecutor(factory)) {

            List<? extends Future<?>> futures = IntStream.range(0, 1000)
                    .mapToObj(i -> executor.submit(r.apply(i)))
                    .toList();

            for (Future<?> future : futures) {
                future.get();
            }

        }
    }


}

