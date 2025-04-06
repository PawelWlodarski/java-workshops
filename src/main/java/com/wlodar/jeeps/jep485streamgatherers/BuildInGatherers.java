package com.wlodar.jeeps.jep485streamgatherers;

import com.wlodar.WorkshopPrinter;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;

public class BuildInGatherers {

    public static void main(String[] args) {
        WorkshopPrinter.title("Built in gatherers");

        var data= List.of(1,2,3,4,5,6,7,8,9);

        WorkshopPrinter.subtitle("fold");
        Supplier<String> zero=() -> "";
        BiFunction<String,Integer,String> folder=(state, i) -> state+i;
        data.stream()
                .gather(Gatherers.fold(zero, folder)).findFirst().ifPresent(System.out::println);

        WorkshopPrinter.subtitle("windowFixed");
        data.stream().gather(Gatherers.windowFixed(3)).forEach(System.out::println);

        WorkshopPrinter.subtitle("windowSliding");
        data.stream().gather(Gatherers.windowSliding(3)).forEach(System.out::println);

    }

    private static void gatherersComposition(){
        Gatherer<String,?, Optional<Integer>> parseInts= null;
        Gatherer<Optional<Integer>,?,Integer> dropEmpty =null;

        Gatherer<String, ?, Integer> composition = parseInts.andThen(dropEmpty);
    }
}
