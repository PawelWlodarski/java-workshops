package com.wlodar;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WorkshopPrinter {

    public static void title(String title){
        var titleLength=title.length();
        var stars= Stream.generate(()->"*").limit(titleLength).collect(Collectors.joining());
        System.out.println("**********"+stars+"**********");
        System.out.println("*****     "+title+"     *****");
        System.out.println("**********"+stars+"**********");
    }

    public static void subtitle(String title){
        System.out.println(System.lineSeparator() +  "-----  "+title);
    }

    public static void print(String text){
        System.out.println(text);
    }

    public static void print(String prefix, String text){
        System.out.println(prefix +" : "+text);
    }

    public static void print(String prefix, Object text){
        System.out.println(prefix +" : "+text);
    }

    public static void printfThread(String format, String message){
        System.out.printf("[%s] " + format + "%n", currentThreadLabel(), message);
    }

    public static void printThread(String message){
        System.out.printf("[%s] %s%n", currentThreadLabel(), message);
    }

    private static String currentThreadLabel() {
        return threadLabel(Thread.currentThread());
    }

    private static String threadLabel(Thread thread) {
        String kind = thread.isVirtual() ? "virtual" : "platform";
        String name = thread.getName().isBlank() ? "<unnamed>" : thread.getName();
        return kind + " | id=" + thread.threadId() + " | " + name + " | raw=" + thread;
    }

}
