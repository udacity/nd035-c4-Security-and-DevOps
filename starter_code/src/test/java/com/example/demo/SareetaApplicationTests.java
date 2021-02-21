package com.example.demo;

import com.example.demo.controllers.*;
import com.example.demo.security.UserAuthTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        UserControllerTest.class,
        CartControllerTest.class,
        ItemControllerTest.class,
        OrderControllerTest.class,
        UserAuthTest.class

})
public class SareetaApplicationTests {
    @Test
    public void contextLoads() {
    }

}
