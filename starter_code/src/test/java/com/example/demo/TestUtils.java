package com.example.demo;

import java.lang.reflect.Field;

public class TestUtils {
    public static void injectObjects(Object target, String filedName, Object toInject)  {
        boolean wasPrivate = false;
        Field f = null;
        try {
            f = target.getClass().getDeclaredField(filedName);
            if (!f.isAccessible()){
                f.setAccessible(true);
                wasPrivate = true;

            }
            f.set(target, toInject);
            if (wasPrivate){
                f.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
