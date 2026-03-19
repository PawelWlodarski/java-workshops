package com.wlodar.jeeps.jep513flexibleconstructors;

import com.wlodar.WorkshopPrinter;

public class Jep513EarlyFieldInitializationDemo {

    static void main() {
        WorkshopPrinter.title("JEP 513 - field visible before super()");

        WorkshopPrinter.subtitle("Before: superclass sees default null");
        new EmployeeBeforeJep513(32, "WAR-42");

        WorkshopPrinter.subtitle("After: superclass sees real office id");
        new EmployeeWithJep513(32, "WAR-42");
    }
}

class PersonBase {
    protected final int age;

    PersonBase(int age) {
        this.age = age;

        WorkshopPrinter.print("PersonBase", "Calling show() from superclass constructor");
        show();
    }

    void show() {
        WorkshopPrinter.print("Age", String.valueOf(age));
    }
}

class EmployeeBeforeJep513 extends PersonBase {

    // no initializer here, so default value is null before assignment
    private final String officeId;

    EmployeeBeforeJep513(int age, String officeId) {
        super(age);

        if (age < 18 || age > 67) {
            throw new IllegalArgumentException("Employee age must be between 18 and 67");
        }

        this.officeId = officeId;
    }

    @Override
    void show() {
        WorkshopPrinter.print("Age", String.valueOf(age));
        WorkshopPrinter.print("Office", String.valueOf(officeId));
    }
}

class EmployeeWithJep513 extends PersonBase {

    // important: declared without initializer
    private String officeId;

    EmployeeWithJep513(int age, String officeId) {
        // JEP 513 allows safe work before super()

        if (age < 18 || age > 67) {
            throw new IllegalArgumentException("Employee age must be between 18 and 67");
        }

        // legal in constructor prologue:
        // assignment to an uninitialized field declared in this class
        this.officeId = officeId;

        super(age);
    }

    @Override
    void show() {
        WorkshopPrinter.print("Age", String.valueOf(age));
        WorkshopPrinter.print("Office", String.valueOf(officeId));
    }
}
