package com.wlodar.jeeps.jep475G1Barriers;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class G1BarrierBenchmark {
    Data[] array;

    @Setup
    public void setup() {
        array = new Data[1_000_000];
        for (int i = 0; i < array.length; i++) {
            array[i] = new Data();
        }
    }

    @Benchmark
    public void updateFields() {
        for (Data d : array) {
            d.x++;
        }
    }

    static class Data {
        int x;
    }
}

