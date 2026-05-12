package com.wlodar.concurrency.structuredconcurrency;

import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.function.Predicate;

import static com.wlodar.WorkshopPrinter.*;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.SUCCESS;

public class JoinersExample {


    //You can implement your own joiner if your really really really want
    //show the interface
    static void main() throws InterruptedException {
        title("StructuredTaskScope Joiners");
        example1();
    }

    private static void example1() throws InterruptedException {
        subtitle("Outer scope: awaitAllSuccessfulOrThrow");

        try (var outerScope = StructuredTaskScope.open(
                Joiner.awaitAllSuccessfulOrThrow()
        )) {
            var f1 = outerScope.fork(JoinersExample::innerScope);

            var f2 = outerScope.fork(() -> {
                printThread("outer side task started");
                Thread.sleep(500);
                printThread("outer side task finished");
                return "outer side task result";
            });

            outerScope.join();

            print("outer f1 result", f1.get());
            print("outer f2 result", f2.get());
        }
    }

    private static String innerScope() throws InterruptedException {
        subtitle("Inner scope: anySuccessfulOrThrow");

        try (var innerScope = StructuredTaskScope.open(Joiner.<String>anySuccessfulOrThrow())) {
            innerScope.fork(() -> {
                printThread("slow service started");
                Thread.sleep(2_000);
                printThread("slow service finished");
                return "slow service result";
            });

            innerScope.fork(() -> {
                printThread("fast service started");
                Thread.sleep(700);
                printThread("fast service finished");
                return "fast service result";
            });

            innerScope.fork(() -> {
                printThread("broken service started");
                Thread.sleep(300);
                throw new IllegalStateException("broken service failed");
            });

            //Here we don't know which one finished so join returns the value
            //for the default one we have : Joiner<T, Void>
            // there is similar Joiner.allSuccessfulOrThrow() which returns Joiner<T, List<T>>
            String firstSuccessfulResult = innerScope.join();

            print("inner result", firstSuccessfulResult);

            Integer goodEnoughScore = innerMostScope();

            return firstSuccessfulResult + ", good enough score = " + goodEnoughScore;
        }
    }

    private static Integer innerMostScope() throws InterruptedException {
        subtitle("Inner-most scope: allUntil");

        Predicate<Subtask<Integer>> predicate =
                subtask -> subtask.state() == SUCCESS && subtask.get() >= 80;

        try (var innerMostScope = StructuredTaskScope.open(Joiner.allUntil(predicate))) {
            innerMostScope.fork(() -> scoreFrom("model-a", 1_500, 42));
            innerMostScope.fork(() -> scoreFrom("model-b", 900, 87));
            innerMostScope.fork(() -> scoreFrom("model-c", 2_500, 95));

            //this time result is a list gathered until predicate
            List<Subtask<Integer>> subtasks = innerMostScope.join();

            List<Integer> successfulScores = subtasks.stream()
                    .filter(subtask -> subtask.state() == SUCCESS)
                    .map(Subtask::get)
                    .toList();

            print("successful scores", successfulScores);

            return successfulScores.stream()
                    .max(Integer::compareTo)
                    .orElseThrow();
        }
    }

    private static Integer scoreFrom(String name, int delayMillis, int score) throws InterruptedException {
        try {
            printThread(name + " started");
            Thread.sleep(delayMillis);
            printThread(name + " returned score " + score);
            return score;
        } catch (InterruptedException e) {
            printThread(name + " was cancelled");
            throw e;
        }
    }
}