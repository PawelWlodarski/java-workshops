package com.wlodar.concurrency.virtualthreads;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class UnsolvedNativeApiCallBlock {

    private static final MethodHandle SLEEP = sleepHandle();

    static void main() throws Exception {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = IntStream.range(0, 30).mapToObj(i ->
                    executor.submit(() -> {
                        System.out.println(i + " before native sleep");
                        nativeSleep(30);
                        System.out.println(i + " after native sleep");
                    })
            ).toList();

            for (Future<?> future : futures) {
                future.get();
            }
        }
    }

    private static void nativeSleep(int seconds) {
        try {
            int ignored = (int) SLEEP.invokeExact(seconds);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static MethodHandle sleepHandle() {
        try {
            var linker = Linker.nativeLinker();

            var sleep = SymbolLookup.loaderLookup()
                    .find("sleep")
                    .or(() -> linker.defaultLookup().find("sleep"))
                    .orElseThrow();

            return linker.downcallHandle(
                    sleep,
                    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT)
            );
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}