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
        System.out.println("-----  "+title);
    }

    public static void print(String text){
        System.out.println(text);
    }

    public static void print(String prefix, String text){
        System.out.println(prefix +" : "+text);
    }

}
