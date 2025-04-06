package com.wlodar.jeeps.jep485streamgatherers;

import com.wlodar.WorkshopPrinter;

import java.util.List;
import java.util.stream.Gatherer;

public class StreamGatherersParsingExample {


    public static void main() {
        WorkshopPrinter.title("Gatherers parsing example");
        var candidates = List.of("1", "2", "wrong", "3", "bad", "5");

        var result1 = candidates
                .stream()
                .filter(e -> {
                            try {
                                Integer.parseInt(e);
                                return true;
                            } catch (NumberFormatException ex) {
                                return false;
                            }
                        }
                ).map(Integer::parseInt)
                .toList();

        WorkshopPrinter.subtitle("standard approach");
        System.out.println(result1);

        var result2 = candidates.stream()
                .gather(Gatherer.of(
                        (_, e, downstream) -> {
                            try {
                                downstream.push(Integer.parseInt(e));
                            } catch (NumberFormatException _) {
                            }
                            return true;
                        }
                )).toList();

        WorkshopPrinter.subtitle("gatherer approach");
        System.out.println(result2);
    }
}
