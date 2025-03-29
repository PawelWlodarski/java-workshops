package com.wlodar.jeeps.jep475G1Barriers;

public class G1BarrierStress {
    static class Data {
        int x;
    }

    public static void main(String[] args) {
        Data[] array = new Data[1_000_000];
        for (int i = 0; i < array.length; i++) {
            array[i] = new Data();
        }

        long start = System.nanoTime();

        for (int round = 0; round < 1000; round++) {
            for (Data d : array) {
                d.x += 1;
            }
        }

        long time = System.nanoTime() - start;
        System.out.println("Done in: " + time / 1_000_000 + " ms");
    }
}

