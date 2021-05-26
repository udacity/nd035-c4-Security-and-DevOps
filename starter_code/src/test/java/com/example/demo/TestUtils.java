package com.example.demo;

import java.lang.reflect.Field;

public class TestUtils {
    public static void injectObjects(Object target, String fieldName, Object toInject) {
        boolean wasPrivate = false;
        try {
            Field declaredField = target.getClass().getDeclaredField(fieldName);

            if(!declaredField.isAccessible()){
                declaredField.setAccessible(true);
                wasPrivate = true;
            }

            declaredField.set(target, toInject);
            if(wasPrivate){
                declaredField.setAccessible(false);
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
