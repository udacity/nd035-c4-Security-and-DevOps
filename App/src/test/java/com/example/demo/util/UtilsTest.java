package com.example.demo.util;

import java.lang.reflect.Field;

public class UtilsTest {

    public static boolean isEqual(Object obj1,Object obj2) {
        if (obj1 == obj2) return true;

        if (obj1 == null || obj2 == null) return false;

        if (obj1.getClass() != obj2.getClass()) return false;

        Field[] fields = obj1.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object item1value = field.get(obj1);
                Object item2value = field.get(obj2);
                if (!item1value.equals(item2value)) return false;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}
