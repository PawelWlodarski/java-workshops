package com.wlodar.jeeps.jep515aotmethodprofiling;

import java.util.List;
import java.util.stream.Collectors;

public class HelloStreamWarmup {

    static String greeting(int n) {
        var words = List.of("Hello", "" + n, "world!");
        return words.stream()
                .filter(w -> !w.contains("0"))
                .collect(Collectors.joining(", "));
    }

    static void main() {
        for (int i = 0; i < 100_000; i++) {
            greeting(i);
        }
        System.out.println(greeting(0));
    }
}