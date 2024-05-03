package com.example.demo;

import java.lang.reflect.Field;

public class TestUtils {

    public static void injectObjects(Object target, String fieldName, Object toInject) {
        boolean wasPrivate = false;

        try {
            Field declaredFields = target.getClass().getDeclaredField(fieldName);
            //check if declaredFields is accessible or not
            if (!declaredFields.isAccessible()) {
                declaredFields.setAccessible(true);
                wasPrivate = true;
            }
            declaredFields.set(target, toInject);
            if(wasPrivate) {
                declaredFields.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
