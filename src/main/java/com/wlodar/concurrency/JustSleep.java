package com.wlodar.concurrency;

public class JustSleep {

    public static void around(int miliseconds) {
        try {
            Thread.sleep(miliseconds + Math.round(Math.random() * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
