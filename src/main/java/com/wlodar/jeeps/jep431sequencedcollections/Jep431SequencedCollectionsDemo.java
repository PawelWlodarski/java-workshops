package com.wlodar.jeeps.jep431sequencedcollections;

import com.wlodar.WorkshopPrinter;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

public class Jep431SequencedCollectionsDemo {

    static void main() {
        WorkshopPrinter.title("JEP 431: Sequenced Collections");

        demoList();
        demoLinkedHashSet();
        demoLinkedHashMap();
    }

    private static void demoList() {
        WorkshopPrinter.subtitle("List now has uniform first/last/reversed operations");

        List<String> languages = List.of("Java", "Kotlin", "Scala");

        WorkshopPrinter.print("Original list", languages);
        WorkshopPrinter.print("First", languages.getFirst());
        WorkshopPrinter.print("Last", languages.getLast());
        WorkshopPrinter.print("Reversed view", languages.reversed());
    }

    private static void demoLinkedHashSet() {
        WorkshopPrinter.subtitle("LinkedHashSet finally behaves like a real sequence");

        var frameworks = new LinkedHashSet<String>();
        frameworks.add("Spring");
        frameworks.add("Micronaut");
        frameworks.add("Quarkus");

        WorkshopPrinter.print("Original set", frameworks);
        WorkshopPrinter.print("First", frameworks.getFirst());
        WorkshopPrinter.print("Last", frameworks.getLast());
        WorkshopPrinter.print("Reversed view", frameworks.reversed());

        frameworks.addFirst("Helidon");
        frameworks.addLast("Dropwizard");

        WorkshopPrinter.print("After addFirst/addLast", frameworks);

        // interesting part: re-adding existing element with addFirst/addLast repositions it
        frameworks.addFirst("Quarkus");
        WorkshopPrinter.print("After moving existing 'Quarkus' to front", frameworks);
    }

    private static void demoLinkedHashMap() {
        WorkshopPrinter.subtitle("LinkedHashMap also gets first/last/reversed operations");

        var versions = new LinkedHashMap<String, Integer>();
        versions.put("Java 17", 2021);
        versions.put("Java 21", 2023);
        versions.put("Java 25", 2025);

        WorkshopPrinter.print("Original map", versions);
        WorkshopPrinter.print("First entry", versions.firstEntry());
        WorkshopPrinter.print("Last entry", versions.lastEntry());
        WorkshopPrinter.print("Reversed view", versions.reversed());

        versions.putFirst("Java 11", 2018);
        versions.putLast("Java 26", 2026);

        WorkshopPrinter.print("After putFirst/putLast", versions);

        // reposition existing key
        versions.putFirst("Java 25", 2025);
        WorkshopPrinter.print("After moving existing 'Java 25' to front", versions);
    }
}
