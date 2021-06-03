package com.example.demo;

import java.lang.reflect.Field;

public class TestUtils {
    public static void injectObjects(Object target, String fieldName, Object toInject) {

        boolean wasPrivate = false;

        try {
            Field f = target.getClass().getDeclaredField(fieldName);

            if(!f.isAccessible()) {
                f.setAccessible(true);
                wasPrivate = true;
            }
            f.set(target, toInject);
            if(wasPrivate){
                f.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

//Notes:
//mock()/@Mock - To create mock object. Here, mock() is an overloaded static method which accepts Class<T> classToMock as an argument and correspondingly returns a mock instance.
//when()/given() - To specify the behaviour of mock object. when() method takes a particular method X as argument and set the expected (mocked) return value as Y. Following are the few examples:
//when(mock.someMethod()).thenReturn(200);
////parameterized methods as argument
//when(mock.someMethod(anyString())).thenReturn(200);
////throw an exception
//when(mock.someMethod("some argument")).thenThrow(new RuntimeException());
//spy()/ @Spy - It is used for partial mocking. It means that the real methods are invoked but still can be verified and stubbed.
//There are few more methods and annotations available, such as @InjectMocks, verify()
//Note that if a project contains private methods to test, then we can't use Mockito as it does not mock private methods. Mockito assumes that private methods don't exist from the viewpoint of testing.
//There are few other mocking frameworks available for Java, such as JMock, EasyMock, and Powermock. It is a matter of choice of the developer based on preferences such as Powermock can even mock private methods.
