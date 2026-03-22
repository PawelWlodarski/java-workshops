package com.wlodar.jeeps.jep409sealed;

import com.wlodar.WorkshopPrinter;

import java.util.function.Function;

public class Jep409OptionDemo {

    static void main() {
        WorkshopPrinter.title("JEP 409: Sealed Classes (Option)");

        optionDemo();
    }

    static void optionDemo() {
        WorkshopPrinter.subtitle("Option: Some / None");

        Option<String> value1 = new Some<>("Hello");
        Option<String> value2 = new None<>();

        WorkshopPrinter.print("value1", describe(value1));
        WorkshopPrinter.print("value2", describe(value2));

        WorkshopPrinter.print("value1.map", map(value1, String::toUpperCase));
        WorkshopPrinter.print("value2.map", map(value2, String::toUpperCase));
    }

    static String describe(Option<?> option) {
        return switch (option) {
            case Some<?> s -> "Some(" + s.value() + ")";
            case None<?> n -> "None";
        };
    }

    //Limitation of java and its on site variance declaration,
    //there is no bottom type like nothing in
    // Scala so we can not represent None just like bottom option Option<Nothing>
    static <T, R> Option<R> map(Option<T> option, Function<T, R> mapper) {
        return switch (option) {
            case Some<T> s -> new Some<>(mapper.apply(s.value()));
            case None<T> _ -> new None<>();
        };
    }

    // --- sealed hierarchy ---

    sealed interface Option<T> permits Some, None {
    }

    record Some<T>(T value) implements Option<T> {}

    record None<T>() implements Option<T> {}

}
