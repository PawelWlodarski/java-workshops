package com.wlodar.concurrency.scopedvalues;

import com.wlodar.WorkshopPrinter;

public class ScopedValuesInDepth {

    private static final ScopedValue<String> REQUEST_ID = ScopedValue.newInstance();
    private static final ScopedValue<String> USER_ID = ScopedValue.newInstance();

    static void main() {
        //carrier has arrived
        ScopedValue.Carrier carrier = ScopedValue.where(REQUEST_ID, "req-123");

        ScopedValue.Carrier carrier2 = carrier.where(USER_ID, "123");


        carrier2.run(() -> {
            WorkshopPrinter.print("REQUEST_ID", REQUEST_ID.get());
            WorkshopPrinter.print("USER_ID", USER_ID.get());

            var internalCarrier=ScopedValue.where(USER_ID, "456");
            internalCarrier.run(() -> {
                WorkshopPrinter.print("internal REQUEST_ID", REQUEST_ID.get());
                WorkshopPrinter.print("internal USER_ID", USER_ID.get());
            });
        });


        carrier2.run(() -> {
            WorkshopPrinter.print("REQUEST_ID", REQUEST_ID.get());
            WorkshopPrinter.print("USER_ID", USER_ID.get());
        });

    }

}
