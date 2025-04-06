package com.wlodar.jeeps.jep485streamgatherers;

import com.wlodar.WorkshopPrinter;

import java.util.function.Function;
import java.util.stream.Gatherer;
import java.util.stream.IntStream;

public class GathererStreamRejectionDemo {

    public static void main(String[] args) {
        WorkshopPrinter.title("Stream rejecting demo");

        WorkshopPrinter.subtitle("naive");
        var r1=IntStream.range(0,50)
                .boxed()
                .map(i -> {
                    System.out.println("mapping : "+i);
                    return i+1;
                })
                .gather(naiveLimit(5))
                .toList();

        System.out.println(r1);

        WorkshopPrinter.subtitle("proper");
        var r2=IntStream.range(0,50)
                .boxed()
                .map(i -> {
                    System.out.println("mapping : "+i);
                    return i+1;
                })
                .gather(properLimit(5))
                .toList();

        System.out.println(r2);
    }


    public static <T> Gatherer<T,?,T> naiveLimit(long maxSize){
        class State{long count;}
        return Gatherer.ofSequential(
                State::new ,
                (currentstate,e,downstream) -> {
                    if(currentstate.count<maxSize) {
                        currentstate.count++;
                        downstream.push(e);
                    }
                    return  true;
                }
        );
    }

    public static <T> Gatherer<T,?,T> properLimit(long maxSize){
        class State{long count;}
        return Gatherer.ofSequential(
                State::new ,
                (currentstate,e,downstream) -> {
                    if(downstream.isRejecting()) return false;

                    if(currentstate.count<maxSize) {
                        downstream.push(e);
                    }
                    currentstate.count++;
                    return  currentstate.count<maxSize;
                }
        );
    }


}
