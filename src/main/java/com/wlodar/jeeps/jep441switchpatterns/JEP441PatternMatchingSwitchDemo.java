package com.wlodar.jeeps.jep441switchpatterns;

import com.wlodar.WorkshopPrinter;

public class JEP441PatternMatchingSwitchDemo {

    static void main() {
        WorkshopPrinter.title("JEP 441: Pattern Matching for switch");

        formatterDemo();
        notificationDemo();
        sealedDemo();
    }

    // ------------------------------------------------------------
    // 1. OFFICIAL JEP EXAMPLE (before vs after)
    // ------------------------------------------------------------

    static void formatterDemo() {
        WorkshopPrinter.subtitle("Formatter example (JEP page)");

        Object[] values = {42, 42L, 3.14, "hello", true};

        for (Object v : values) {
            WorkshopPrinter.print("Old", formatter(v));
            WorkshopPrinter.print("New", formatterPatternSwitch(v));
        }
    }

    // Prior to Java 21
    static String formatter(Object obj) {
        String formatted ;
        if (obj instanceof Integer i) {
            formatted = String.format("int %d", i);
        } else if (obj instanceof Long l) {
            formatted = String.format("long %d", l);
        } else if (obj instanceof Double d) {
            formatted = String.format("double %f", d);
        } else if (obj instanceof String s) {
            formatted = String.format("String %s", s);
        }else {
            formatted = obj.toString();
        }
        return formatted;
    }

    // As of Java 21
    static String formatterPatternSwitch(Object obj) {
        return switch (obj) {
            case Integer i -> String.format("int %d", i);
            case Long l    -> String.format("long %d", l);
            case Double d  -> String.format("double %f", d);
            case String s  -> String.format("String %s", s);
            default        -> obj.toString();
        };
    }

    // ------------------------------------------------------------
    // 2. REAL-LIFE DEMO (null + guards + type patterns)
    // ------------------------------------------------------------

    static void notificationDemo() {
        WorkshopPrinter.subtitle("Notification routing (real-world)");

        Notification[] notifications = {
                new Email("john@company.com", "Internal"),
                new Email("john@gmail.com", "External"),
                new Sms("+48123123123", "PL message"),
                new Sms("+14155552671", "US message"),
                new Push("device1", 15),
                new Push("device2", 3),
                new Unknown("???"),
                new Missing(),
                null
        };

        for (Notification n : notifications) {
            WorkshopPrinter.print("Route : " + n, route(n));
        }
    }

    static String route(Notification n) {
        return switch (n) {
            case null -> "drop: null notification";

            case Email e when e.address().endsWith("@company.com") -> "internal-email";
            //from JDK22 we can use _ to ignore the value
            case Email e -> "external-email";

            case Sms s when s.number().startsWith("+48") -> "PL-sms-gateway";

            case Sms s -> "global-sms-gateway";

            case Push p when p.priority() >= 10 -> "priority-push";

            case Push p -> "normal-push";
            //this only works with JDK22 _ !!!
            case Unknown _, Missing _-> "manual-review";
        };
    }

    sealed interface Notification permits Email, Missing, Push, Sms, Unknown {}

    record Email(String address, String subject) implements Notification {}
    record Sms(String number, String text) implements Notification {}
    record Push(String deviceId, int priority) implements Notification {}
    record Unknown(String raw) implements Notification {}
    record Missing() implements Notification {}

    // ------------------------------------------------------------
    // 3. SEALED + EXHAUSTIVE SWITCH (no default!)
    // ------------------------------------------------------------

    //add new type to hierarchy and check what happens (error happens)
    static void sealedDemo() {
        WorkshopPrinter.subtitle("Sealed hierarchy (exhaustive switch)");

        Shape[] shapes = {
                new Circle(2),
                new Rectangle(3, 4)
        };

        for (Shape s : shapes) {
            WorkshopPrinter.print("Area", area(s));
        }
    }

    static double area(Shape shape) {
        return switch (shape) {
            case Circle c -> Math.PI * c.radius() * c.radius();
            case Rectangle r -> r.width() * r.height();
        };
    }

    sealed interface Shape permits Circle, Rectangle {}

    record Circle(double radius) implements Shape {}
    record Rectangle(double width, double height) implements Shape {}
}
