package com.wlodar.jeeps.jep485streamgatherers;

import java.util.List;
import java.util.stream.Gatherer;

public class StreamGatherersIntro {

    private record Artist(String name, List<String> songs) {}
    private record SongPair(String artist, String song) {}

    private  static final List<Artist> artists = List.of(
            new Artist("Adele", List.of("Hello", "Rolling in the Deep")),
            new Artist("Coldplay", List.of("Yellow", "Fix You")),
            new Artist("Imagine Dragons", List.of("Believer", "Radioactive"))
    );

    public static void main(String[] args) {
//        flatMapExample();
//        mapMultiExample();
//        gathererExample();
//        gathererExampleExplicit();
    }

    /**
     * For each pair flatMap has to create another stream for each artist
     * additional memory allocation
     */
    private static void flatMapExample(){
        // Flatten artists into (artist, song) pairs
        artists.stream()
                .flatMap(artist ->
                        artist.songs().stream()
                                .map(song -> new SongPair(artist.name(), song))
                )
                .toList()
                .forEach(System.out::println);

    }

    /**
     * since JDK16 - less memory allocation in comparison to flatMap.
     * Introduces concept of downstream
     */
    private static void mapMultiExample(){
        artists.stream()
                .<SongPair>mapMulti((artist, downstream) -> {
                    for (String song : artist.songs()) {
                        downstream.accept(new SongPair(artist.name(), song));
                    }
                })
                .toList()
                .forEach(System.out::println);
    }

    /**
     * Because Java have real problem with generics we need put them directly in factory method
     */
    private static void gathererExample(){

        var artistSongGatherer=Gatherer.<Artist,SongPair>of(
                (_,artist,downstream) -> {
                    for (String song : artist.songs()) {
                        downstream.push(new SongPair(artist.name(), song));
                    }
                    return true;  //for now ignore this return type
                }
        );

        artists.stream()
                .gather(artistSongGatherer)
                .toList()
                .forEach(System.out::println);
    }

    private static void gathererExampleExplicit(){
        Gatherer.Integrator<Void,Artist,SongPair> integrator= (_,artist,downstream) -> {
            for (String song : artist.songs()) {
                downstream.push(new SongPair(artist.name(), song));
            }
            return true;
        };
        Gatherer<Artist, Void, SongPair> artistSongGatherer = Gatherer.of(integrator);

        artists.stream()
                .gather(artistSongGatherer)
                .toList()
                .forEach(System.out::println);
    }


    //windowFixed as stateful
}
