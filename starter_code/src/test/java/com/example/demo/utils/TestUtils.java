package com.example.demo.utils;

public class TestUtils {
    public static void injectObjects(Object target, String fieldName, Object toInject) throws NoSuchFieldException, IllegalAccessException {
        boolean wasPrivate = false;
        var field = target.getClass().getDeclaredField(fieldName);
        boolean accessibilityFlag = field.canAccess(target);
        field.setAccessible(true);
        field.set(target,toInject);
        field.setAccessible(accessibilityFlag);

    }
}
