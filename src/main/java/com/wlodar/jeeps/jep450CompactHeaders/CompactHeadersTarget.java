package com.wlodar.jeeps.jep450CompactHeaders;

public class CompactHeadersTarget {
    static Object staticObj;

    public static void main(String[] args) throws Exception {
        staticObj = new Object();
        System.out.println("PID: " + ProcessHandle.current().pid());
        Thread.sleep(10 * 60 * 1000); // sleep for 10 minutes to allow attach
    }
}

