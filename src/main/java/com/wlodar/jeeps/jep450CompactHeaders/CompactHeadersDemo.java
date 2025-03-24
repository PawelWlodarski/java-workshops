package com.wlodar.jeeps.jep450CompactHeaders;


import com.wlodar.WorkshopPrinter;

import java.util.ArrayList;
import java.util.List;

public class CompactHeadersDemo {

    static class SmallObject {
        int x;
    }

    static class Synced extends SmallObject {
        @Override
        public synchronized int hashCode() {
            return super.hashCode();
        }
    }

    public static void main(String[] args) {
        WorkshopPrinter.subtitle("Small Object : ");
        System.out.println(org.openjdk.jol.info.ClassLayout.parseClass(SmallObject.class).toPrintable());
        WorkshopPrinter.subtitle("Synced : ");
        System.out.println(org.openjdk.jol.info.ClassLayout.parseClass(Synced.class).toPrintable());
        allocateMemory();
    }


    private static void allocateMemory(){
        WorkshopPrinter.subtitle("Allocating memory");
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 10_000_000; i++) {
            list.add(new SmallObject());
        }
        System.gc();
        WorkshopPrinter.print("Used memory: " , (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024) + " MB");
    }
}
