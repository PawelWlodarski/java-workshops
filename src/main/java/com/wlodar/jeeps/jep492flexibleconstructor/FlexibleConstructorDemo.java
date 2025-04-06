package com.wlodar.jeeps.jep492flexibleconstructor;

import com.wlodar.WorkshopPrinter;

public class FlexibleConstructorDemo {

    public static void main(String[] args) {
        WorkshopPrinter.title("Flexible constructors");
//        validateBeforeCreation();
        illegalCase();
    }


    private static void validateBeforeCreation(){
        var l1=new LongRepresentation(-11);
        var l2=new PositiveOnly(1);
        var l3=new PositiveOnly(-1);
    }

    private static void illegalCase(){
        var illegal=new IllegalCreation(1);
        var o=new Outer();
    }


}



class LongRepresentation {
    public LongRepresentation(long value) {
        System.out.println("Parent received: " + value);
    }
}

class PositiveOnly extends LongRepresentation {
    public PositiveOnly(long value) {
        //prologue
        if (value <= 0) throw new IllegalArgumentException("Must be positive");
        super(value);
        //epilogue
    }
}

class IllegalCreation{
    private long someValue;

    public IllegalCreation(long value) {
        this.someValue = value; //can assign to this before super constructor
        //System.out.println(this); //can not reference this before constructor
        //var i=someValue; //illegal implicit 'this'
        //System.out.println(super.hashCode()); //access to super fields/method also illegal
        super();
    }

    public long getSomeValue() {
        return someValue;
    }
}

class Outer {

    class Inner {}

    Outer() {
//        var x = new Inner();       // Error - implicitly refers to the current instance of Outer
//        var y = this.new Inner();  // Error - explicitly refers to the current instance of Outer
        super();
    }

}