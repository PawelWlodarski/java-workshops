package com.wlodar.jeeps.jep286vars;

import static com.wlodar.WorkshopPrinter.*;
import jakarta.annotation.Nonnull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.System.out;

//Java 10
public class LocalVariableTypeInference {


    public static void main(String[] args) {
        title("Local Variable Type Interference");

        example1ListType();
        example2ForEach();
        example3TryWithResources();
        example4DeclarationToInterface();
        example5AdhocClassDeclaration();
        example6VarInLambda();
    }

    /**
     * Example presents : list type inference
     * //1 - uncomment to see that var inferred proper type
     */
    static void example1ListType(){
        subtitle("Example 1 : Inferring list type");
        //both definition are equivalent
        //List<String> list = Arrays.asList("one", "two", "three");
        var list = Arrays.asList("one", "two", "three");

        //still type safety is preserved
        var stream =list.stream();
        Stream<String> s2=stream;
        s2.forEach(out::println);
        //Stream<Integer> s3=stream;  //1
    }

    /**
     * Use in foreach
     */
    static void example2ForEach(){
        subtitle("Example 2 : Inferring type in for each");
        //here because of var dev may forget to convert stream into array
        var even= IntStream.iterate(0, n-> n+2).limit(10).toArray();
        for (var e: even){
            out.println(e);
        }
    }

    /**
     * Use in try with resources
     */
    static void example3TryWithResources(){
        subtitle("Example 3 : Inferring type in try with resources");
        String data = "Line 1\nLine 2\nLine 3";

        try (var reader = new BufferedReader(new StringReader(data))) {
            reader.lines().forEach(out::println);
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    /**
     * Declaration to interface needs to be explicit
     */
    static List<String> example4DeclarationToInterface(){
        List<String> l1=new ArrayList<>();
        l1=new LinkedList<>();

        var l2=new ArrayList<String>();
        //l2=new LinkedList<>();  l2 is ArrayList
        return l2; // NO IMPLEMENTATION LEAK!!! returns List<String>
    }


    /**
     * Adhoc anonymous class is created to represent Runnable with additional functionality
     */
    static void example5AdhocClassDeclaration(){
        subtitle("Example 5 : ad hoc type calling run twice");
        var v = new Runnable() {
            public void run() {
                out.println("just run");
            }
            void runTwice() { run(); run(); }
        };

        //com.wlodar.jeeps.jep286.LocalVariableTypeInference$1
        out.println(v.getClass().getName());

        v.runTwice();
    }

    /**
     * # Using `var` with Annotations in Java
     *
     * With `var`, we can use annotations.
     *
     * - In the example below, comment out the `.filter(Objects::nonNull)` line to see a warning.
     * - This demonstrates how `@Nonnull` does **not** enforce null checks at runtime.
     *
     */

    static void example6VarInLambda(){
        subtitle("Example 6 : Var in lambda with annotations");
        var result=Stream.of("aa","bbb","ccc",null)
                .filter(Objects::nonNull) //comment to see warnings
                .map((@Nonnull var s) -> s.length())
                .toList();

        out.println(result);
    }

    /**
     * Use var to improve redability of stream chain invocation
     */
    static void example7Readability(){
        subtitle("Example 7 : Simplifying stream chain call");
        record Transaction(String user, int amount, String status) {}
        record ProcessedTransaction(String user, int amount) {}


        List<Transaction> transactions = List.of(
                new Transaction("Alice", 200, "Completed"),
                new Transaction("Bob", 150, "Pending"),
                new Transaction("Charlie", 300, "Completed"),
                new Transaction("Alice", 500, "Completed"),
                new Transaction("Bob", 100, "Failed"),
                new Transaction("Charlie", 50, "Completed")
        );


        var completedTransactions = transactions.stream()
                .filter(tx -> "Completed".equals(tx.status()))
                .map(tx -> new ProcessedTransaction(tx.user(), tx.amount()));

        var totalAmountByUser = completedTransactions
                .collect(Collectors.groupingBy(
                        ProcessedTransaction::user,
                        Collectors.summingInt(ProcessedTransaction::amount)
                ));

        totalAmountByUser.forEach((user, totalAmount) ->
                System.out.println(user + " spent total: $" + totalAmount)
        );
    }



}

//https://www.infoq.com/articles/java-local-variable-type-inference/


