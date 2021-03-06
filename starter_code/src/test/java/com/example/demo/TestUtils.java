package com.example.demo;

import java.lang.reflect.Field;

public class TestUtils {
    public static void injectObjects(Object target, String fieldName, Object toInject) {
        boolean wasPrivate = false;

        try {
            Field field = target.getClass().getDeclaredField(fieldName);

            if (!field.isAccessible()) {
                field.setAccessible(true);
                wasPrivate = true;
            }

            field.set(target, toInject);

            // flag is already set before it reaches this block
            if (wasPrivate) {
                field.setAccessible(false);
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
