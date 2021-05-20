package com.example.demo.utils;

import java.lang.reflect.Field;

public class TestUtils {

    public static void injectObject(Object target, String fieldName, Object toInject){

        boolean wasPrivate = false;

        try {
            Field field = target.getClass().getDeclaredField(fieldName);

            if(!field.isAccessible()){
                field.setAccessible(true);
                wasPrivate = true;
            }

            field.set(target, toInject);

            if(wasPrivate){
                field.setAccessible(false);
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}