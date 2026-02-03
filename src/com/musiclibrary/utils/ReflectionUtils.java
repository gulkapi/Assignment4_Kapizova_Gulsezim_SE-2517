package com.musiclibrary.utils;

import java.lang.reflect.*;
import java.util.Arrays;

public class ReflectionUtils {

    public static void inspectClass(Class<?> clazz) {
        System.out.println("\n=== CLASS INSPECTION: " + clazz.getSimpleName() + " ===");
        System.out.println("Full Name: " + clazz.getName());

        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            System.out.println("Superclass: " + superclass.getSimpleName());
        }

        System.out.println("\nImplemented Interfaces:");
        Arrays.stream(clazz.getInterfaces())
                .forEach(i -> System.out.println("  - " + i.getSimpleName()));

        System.out.println("\nFields:");
        Field[] fields = clazz.getDeclaredFields();
        Arrays.stream(fields)
                .forEach(f -> System.out.printf("  %s %s%n",
                        f.getType().getSimpleName(), f.getName()));

        System.out.println("=".repeat(50));
    }
}
