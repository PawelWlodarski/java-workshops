package com.wlodar.jeeps.jep494simplemoduleimport;

import module java.base;
import module java.sql;

//explicit import because Date is in both SQL and Base
import java.sql.Date;

/**
 * we need only to import module
 */
public class SimpleModuleImportExample {
    public static void main(String[] args) {
        var r=List.of(1,2,3);

        r.stream().map(BigDecimal::new).map(BigDecimal::negate).forEach(System.out::println);
    }

    private static void namesConflict(){
        Date d=new Date(1000);
    }
}
