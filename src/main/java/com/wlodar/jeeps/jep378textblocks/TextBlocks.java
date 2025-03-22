package com.wlodar.jeeps.jep378textblocks;

import static com.wlodar.WorkshopPrinter.*;

//java 15
public class TextBlocks {


    public static void main(String[] args) {
        title("JEP 378 Java 15+: TexBlocks");
        example1Html();
        example2TrailingWhitespaces();
        example3LongLines();
        example4PoorTemplates();
    }

    static void example1Html() {
        subtitle("Printing formatted HTML");
        String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head><title>JEP 378</title></head>
                <body>
                    <h1>Hello, Java Text Blocks!</h1>
                    <p>This HTML is generated using JEP 378.</p>
                </body>
                </html>
                """;

        print(htmlContent);
    }

    static void example2TrailingWhitespaces() {

        subtitle("Controlling whitespaces");

        String sample1 = """
              sample1             
              sample1
                """;

        //whitespaces will be preserved
        String sample2 = """
              sample2 \s\s\s\s\s\s
              sample2
                """;

        print("Normal Whitespaces",sample1);
        print("Special Whitespaces",sample2);
    }

    static void example3LongLines() {

        subtitle("Long lines");

        String textBlock = """
              aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa \
              aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa \
              aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa \
              aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa \
              aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa \
              aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa \
             """;

        print(textBlock);
    }

    static void example4PoorTemplates() {

        subtitle("Poor templates");

        String wannaBeTemplate = """
              select * from $table;
             """.replace("$table","USERS");

        print(wannaBeTemplate);
    }

}
