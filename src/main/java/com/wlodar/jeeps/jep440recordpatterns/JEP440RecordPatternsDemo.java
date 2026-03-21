package com.wlodar.jeeps.jep440recordpatterns;

import com.wlodar.WorkshopPrinter;

public class JEP440RecordPatternsDemo {

    record Point(int x, int y) {}
    record Rectangle(Point topLeft, Point bottomRight) {}
    record User(String name, Address address) {}
    record Address(String city, String country) {}

    static void main() {
        WorkshopPrinter.title("JEP 440: Record Patterns");

        basicDeconstruction();
        nestedDeconstruction();
        failedMatch();
        genericExample();
    }

    private static void basicDeconstruction() {
        WorkshopPrinter.subtitle("1. Basic deconstruction");

        Object obj = new Point(10, 20);

        if (obj instanceof Point(int x, int y)) {
            WorkshopPrinter.print("Matched point", "(" + x + ", " + y + ")");
        }

        // Before JEP 440, with pattern matching you could test type,
        // but still had to call accessors manually:
        //
        // if (obj instanceof Point p) {
        //     int x = p.x();
        //     int y = p.y();
        // }
        //
        // Now the deconstruction is part of the pattern itself.
    }

    private static void nestedDeconstruction() {
        WorkshopPrinter.subtitle("2. Nested record patterns");

        Object shape = new Rectangle(new Point(0, 10), new Point(20, 0));

        if (shape instanceof Rectangle(Point(int x1, int y1), Point(int x2, int y2))) {
            WorkshopPrinter.print("Top-left", "(" + x1 + ", " + y1 + ")");
            WorkshopPrinter.print("Bottom-right", "(" + x2 + ", " + y2 + ")");

            int width = x2 - x1;
            int height = y1 - y2;
            WorkshopPrinter.print("Width", width);
            WorkshopPrinter.print("Height", height);
        }

        Object user = new User("Alice", new Address("Warsaw", "Poland"));

        //Just to show how it works with switch
        switch (user) {
            case User(String name, Address(String city, String country)) -> {
                WorkshopPrinter.print("User", name);
                WorkshopPrinter.print("City", city);
                WorkshopPrinter.print("Country", country);
            }
            default -> {}
        }
    }

    private static void failedMatch() {
        WorkshopPrinter.subtitle("3. No match");

        Object obj = "not a point";

        if (obj instanceof Point(int x, int y)) {
            WorkshopPrinter.print("Matched", "(" + x + ", " + y + ")");
        } else {
            WorkshopPrinter.print("Pattern did not match");
        }
    }

    record Box<T>(T t) { }

    //chatgpt actually didn't know about that one
    private static void genericExample() {
        WorkshopPrinter.subtitle("4. Generics");

        //look that generics are utilised in pattern matching
        //try changing both generics and var
        Box<Box<String>> bbs = new Box<>(new Box<>("Hello"));
        if (bbs instanceof Box<Box<String>>(Box(var s))) {
            WorkshopPrinter.print("String", s);
        }

        //this only worked if I used general type like Obejct. With concrete type there
        //was compilation error which is good
        Object bbi = new Box<>(new Box<>(42));
        if (bbi instanceof Box(Box(String s))) {
            WorkshopPrinter.print("String", s);
        } else {
            WorkshopPrinter.print("Pattern did not match - different generic type");
        }
    }
}
