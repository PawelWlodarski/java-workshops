package com.wlodar.jeeps.jep484classfileapi;

import java.io.IOException;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.MethodModel;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClassFileParser {
    public static void main(String[] args) {
        //path not package
        Path classFilePath = Paths.get("target/classes/com/wlodar/jeeps/jep484classfileapi/SampleForClassApi.class");

        try {
            // Read the class file into a ClassModel
            ClassModel classModel = ClassFile.of().parse(classFilePath);

            // Display basic class information
            System.out.println("Class Name: " + classModel.thisClass().name());
            System.out.println("Super Class: " + classModel.superclass()
                    .map(e -> e.name().stringValue()).orElse("NO Super class") );
            System.out.println("Access Flags: " + classModel.flags());

            // List methods
            System.out.println("\nMethods:");
            for (MethodModel method : classModel.methods()) {
                System.out.println("- " + method.methodName()  + method.methodType());
            }

        } catch (IOException e) {
            System.err.println("Error reading class file: " + e.getMessage());
        }
    }
}
