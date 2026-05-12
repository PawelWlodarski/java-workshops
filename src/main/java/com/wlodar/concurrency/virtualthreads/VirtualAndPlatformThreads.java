package com.wlodar.concurrency.virtualthreads;

import com.wlodar.WorkshopPrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class VirtualAndPlatformThreads {

    private static final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();


    record ThreadInfo(List<Map.Entry<Long, String>> entries){}

    @FunctionalInterface
    interface ThreadInfoListCallable extends Callable<ThreadInfo> {}

    static void main() {

        IntFunction<ThreadInfoListCallable> runnableFactory = i -> () -> {
            List<Map.Entry<Long, String>> threadsInfo=new ArrayList<>();
            WorkshopPrinter.printThread("first sleep : "+i);
            sleepAround(100);
            addCurrentThreadInfo(threadsInfo);
            WorkshopPrinter.printThread("second sleep : " +i);
            sleepAround(200);
            addCurrentThreadInfo(threadsInfo);
            WorkshopPrinter.printThread("third sleep : "+i);
            sleepAround(300);
            addCurrentThreadInfo(threadsInfo);

            return new ThreadInfo(threadsInfo);
        };

        var threadInfoResultMatrix=IntStream.range(0, 15)
                .mapToObj(i -> executorService.submit(runnableFactory.apply(i)))
                .map(VirtualAndPlatformThreads::justGetIt)
                .map(ThreadInfo::entries)
                .flatMap(List::stream);

        var result=threadInfoResultMatrix.collect(
                Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                Map.Entry::getValue,
                                Collectors.toSet()
                        )
                )
        );

        WorkshopPrinter.subtitle("result");
        result.forEach((k,v) -> {
            System.out.println("Thread id: "+k+" was carried by the following platform threads: ");
            v.forEach(System.out::println);
        });

    }

    private static ThreadInfo justGetIt(Future<ThreadInfo> f){
        try {
            return f.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addCurrentThreadInfo(List<Map.Entry<Long, String>> threadsInfo) {
        threadsInfo.add(Map.entry(Thread.currentThread().threadId(), Thread.currentThread().toString()));
    }

    private static void sleepAround(int millis) {
        try {
            Thread.sleep(millis + (int) (100 * Math.random()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
