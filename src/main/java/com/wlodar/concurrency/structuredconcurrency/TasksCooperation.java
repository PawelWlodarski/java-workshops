package com.wlodar.concurrency.structuredconcurrency;

import com.wlodar.WorkshopPrinter;

import java.util.concurrent.StructuredTaskScope;

import static java.util.concurrent.StructuredTaskScope.Joiner.anySuccessfulOrThrow;

public class TasksCooperation {

    static void main() throws InterruptedException {
        example1CancellingThroughExceptions();
//        example2NoSleeping();
//        example3NoSleepingButChecking();
    }

    private static void example1CancellingThroughExceptions() throws InterruptedException{
        try(var scope = StructuredTaskScope.open(anySuccessfulOrThrow())) {
            scope.fork(() -> {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        WorkshopPrinter.print("f1 interrupted but exception ignored");
                    }
                    WorkshopPrinter.print("f1 still working");
                }

                //welcome in java checked exception world , with return null this is callable which
                //declares V call() throws Exception;
                return null;
            });

            scope.fork(() -> {
                for (int i = 0; i < 3; i++) {
                    Thread.sleep(100);
                    WorkshopPrinter.print("f2 still working");
                }
                return null;
            });

            Thread.sleep(350);


            scope.join();
        }
    }



    private static void example2NoSleeping() throws InterruptedException{
        try(var scope = StructuredTaskScope.open(anySuccessfulOrThrow())) {
            scope.fork(() -> {
                long number = 0;
                while (true) {
                    if(number % 10000 == 0){
                        WorkshopPrinter.print("f1 still calcualting prime:" + number);
                    }
                    isPrime(number);
                    number++;
                }
            });

            scope.fork(() -> {
                for (int i = 0; i < 3; i++) {
                    Thread.sleep(100);
                    WorkshopPrinter.print("f2 still working");
                }
                WorkshopPrinter.print("f2 finished");
                return null;
            });

            Thread.sleep(350);


            scope.join();
        }

    }

    private static void example3NoSleepingButChecking() throws InterruptedException{
        try(var scope = StructuredTaskScope.open(anySuccessfulOrThrow())) {
            scope.fork(() -> {
                long number = 0;
                while (!Thread.currentThread().isInterrupted()) {
                    if(number % 10000 == 0){
                        WorkshopPrinter.print("f1 still calcualting prime:" + number);
                    }
                    isPrime(number);
                    number++;
                }
            });

            scope.fork(() -> {
                for (int i = 0; i < 3; i++) {
                    Thread.sleep(100);
                    WorkshopPrinter.print("f2 still working");
                }
                WorkshopPrinter.print("f2 finished");
                return null;
            });

            Thread.sleep(350);


            scope.join();
        }

    }


    private static boolean isPrime(long number) {
        if (number < 2) {
            return false;
        }

        for (long divisor = 2; divisor < number; divisor++) {
            if (number % divisor == 0) {
                return false;
            }
        }

        return true;
    }

}
