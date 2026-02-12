package com.wlodar.jeeps.jep454foreignfunctions;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;


public class NativeCallDemo {
    public static void main(String[] args) throws Throwable {
        /**
         * Linker is new "gate" for accessing native functions.
         * Also it is an interesting sealed interface declaration
         * public sealed interface Linker permits AbstractLinker {
         */
        var linker = Linker.nativeLinker();

        //A symbol lookup retrieves the address of a symbol in one or more libraries.
        SymbolLookup stdlib = linker.defaultLookup();

        //MethodHandle is an "old school" mechanism from 1.7 so now we see how nicely it is integrated
        MethodHandle strlen = linker.downcallHandle(
                stdlib.find("strlen").orElseThrow( () -> new RuntimeException("Symbol not found")),
                FunctionDescriptor.of(ValueLayout.JAVA_LONG,
                        ValueLayout.ADDRESS)
        );

        //Arena is an abstraction over borrowed memory
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment str = arena.allocateFrom("Hello FFM");

            long len = (long) strlen.invoke(str);
            System.out.println("Length = " + len);
        }
    }
}

