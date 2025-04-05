package com.wlodar.jeeps.jep488primitivesinpatterns;

import com.wlodar.WorkshopPrinter;

import java.util.Map;

public class PrimitiveNestedPatterns {
    sealed interface JsonValue permits JsonString, JsonNumber, JsonObject {}

    record JsonString(String s) implements JsonValue {}
    record JsonNumber(double d) implements JsonValue {}
    record JsonObject(Map<String, JsonValue> map) implements JsonValue {}

    public static void main(String[] args) {
        var json = new JsonObject(Map.of(
                "name", new JsonString("Alice"),
                "age", new JsonNumber(30) // int boxed as double
        ));

        if (json instanceof JsonObject(var map)
                && map.get("name") instanceof JsonString(String name)
                && map.get("age") instanceof JsonNumber(int age)) {
            WorkshopPrinter.print("Matched with int age! Name: " + name + ", Age: " + age);
        } else {
            WorkshopPrinter.print("Pattern match failed");
        }

        // Fails because age is float and not int
        var json2 = new JsonObject(Map.of("age", new JsonNumber(3.14)));
        if (json2 instanceof JsonObject(var map2)
                && map2.get("age") instanceof JsonNumber(int _)) {
            WorkshopPrinter.print("This won't be printed");
        } else {
            WorkshopPrinter.print("Correctly skipped lossy match for double 3.14");
        }

        WorkshopPrinter.subtitle("switch example");
        print("root",json);

        var json3 = new JsonObject(Map.of(
                "sample1", new JsonNumber(2),
                "sample2", new JsonNumber(30.0),
                "sample3", new JsonNumber(30.5),
                "sample4", new JsonNumber(30000),
                "sample5", new JsonNumber(30L),
                "sample6", new JsonNumber((short)30)
        ));

        WorkshopPrinter.subtitle("primitives casting example");
        print("root",json3);
    }

    private static void print(String name,JsonValue json){
        switch (json){
            case JsonObject(Map<String, JsonValue> map) -> map.forEach(PrimitiveNestedPatterns::print);
            case JsonNumber(byte i) when i <3 -> WorkshopPrinter.print(name+" : short<3 - "+i);
            case JsonNumber(byte i) -> WorkshopPrinter.print(name+" : short - "+i);
            case JsonNumber(double d) -> WorkshopPrinter.print(name+" : double - "+d); //comment to see not exhaustive
            case JsonString(String s) -> WorkshopPrinter.print(name, s);
        }
    }
}
