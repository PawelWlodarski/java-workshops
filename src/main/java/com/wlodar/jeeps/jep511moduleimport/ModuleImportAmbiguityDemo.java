package com.wlodar.jeeps.jep511moduleimport;

import com.wlodar.WorkshopPrinter;
import module java.base;
import module java.sql;
import java.sql.Date;

public class ModuleImportAmbiguityDemo {

    static void main() {
        WorkshopPrinter.title("JEP 511 - ambiguous imports");

        showProblem();
        showSolution();
    }

    static void showProblem() {
        WorkshopPrinter.subtitle("Problem");

        WorkshopPrinter.print("""
                import module java.base;
                import module java.sql;

                Date d = ...   // compile error: ambiguous
                """);

        WorkshopPrinter.print("Why",
                "java.util.Date comes from java.base and java.sql.Date comes from java.sql");
    }

    static void showSolution() {
        WorkshopPrinter.subtitle("Solution");

        Date sqlDate = Date.valueOf("2026-03-17");

        WorkshopPrinter.print("""
                import module java.base;
                import module java.sql;
                import java.sql.Date;

                Date d = Date.valueOf("2026-03-17");   // now unambiguous
                """);

        WorkshopPrinter.print("Resolved type", sqlDate.getClass().getName());
        WorkshopPrinter.print("Value", sqlDate);
    }
}
