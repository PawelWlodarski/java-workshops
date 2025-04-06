package com.wlodar.jeeps.jep485streamgatherers;

import com.wlodar.WorkshopPrinter;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

public class ReimplementPopularOperations {

    public static void main(String[] args) {
        WorkshopPrinter.title("reimplement popular operations with gatherers");

        WorkshopPrinter.subtitle("map");
        var fromMapper=Stream.of(1,2,3,4,5).gather(mapWithGatherer(i -> "value :"+i)).toList();
        System.out.println(fromMapper);

        WorkshopPrinter.subtitle("multiMap");
        var fromMultiMapper=Stream.of(1,2,3,4,5)
                .gather(multiMapWithGatherer(
                        (i,downstream) -> downstream.accept("value :"+i)
                )).toList();
        System.out.println(fromMultiMapper);

        WorkshopPrinter.subtitle("limit");
        var limited=Stream.of(1,2,3,4,5).gather(limit(3)).toList();
        System.out.println(limited);

    }


    public static <T,R> Gatherer<T,?,R> mapWithGatherer(Function<? super T, ? extends R> mapper){
        return Gatherer.of(
                (_,e,downstream) -> downstream.push(mapper.apply(e))
        );
    }

    /**
     * Remember mapMulti : mapMulti((artist, downstream) -> { ..}
     * Artlist -> T
     * SongPair -> R
     * downstream ->  Consumer<SongPair
     *
     * below ? super and ? extends omitted for clarity
     */
    public static <T,R> Gatherer<T,?,R>  multiMapWithGatherer(BiConsumer<T, Consumer<R>> mapper){
        return Gatherer.of((_,element,downstream) -> {
            mapper.accept(element,downstream::push);
            return true;
        });
    }

    public static <T> Gatherer<T,?,T> limit(long maxSize){
        class State{long left=maxSize;}
        return Gatherer.ofSequential(
                State::new , //this time gatherer is stateful
                (currentstate,e,downstream) -> {
                    if(currentstate.left<=0) return false;
                    currentstate.left= currentstate.left-1;
                    return  downstream.push(e);
                }
        );
    }

}
