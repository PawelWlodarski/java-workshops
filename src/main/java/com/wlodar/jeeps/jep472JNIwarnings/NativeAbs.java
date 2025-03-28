package com.wlodar.jeeps.jep472JNIwarnings;

public class NativeAbs {
    public native int abs(int x);

    static {
        System.loadLibrary("nativeabs"); // link to libc
    }

    public static void main(String[] args) {
        int result = new NativeAbs().abs(-42);
        System.out.println("abs(-42) = " + result);
    }
}

