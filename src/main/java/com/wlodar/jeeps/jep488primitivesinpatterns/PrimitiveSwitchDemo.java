package com.wlodar.jeeps.jep488primitivesinpatterns;


import com.wlodar.WorkshopPrinter;

public class PrimitiveSwitchDemo {

    public static void main(String[] args) {
        testValue(42);
        testValue(3.14);
        testValue(false);
        testValue("hello");
    }

    static void testValue(Object value) {
        String result = switch (value) {
            //case 42 -> "for now forbidden, planned for the future";
            case int i       -> "Matched int: " + i;
            case double d    -> "Matched double: " + d;
            case boolean b   -> "Matched boolean: " + b;
            default          -> "Other type: " + value;
        };
        WorkshopPrinter.print(result);
    }
}
