package com.example.demo.utils;

import java.lang.reflect.Field;

public class TestUtils {
    public static void injectObjects(Object target, String fieldName, Object toInject) throws NoSuchFieldException, IllegalAccessException {
        Field field = target.getClass().getDeclaredField(fieldName);
        boolean accessibilityFlag = field.isAccessible();
        field.setAccessible(true);
        field.set(target, toInject);
        field.setAccessible(accessibilityFlag);
    }
}
