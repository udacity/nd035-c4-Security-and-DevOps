package com.example.demo;

import com.example.demo.controllers.CartControllerTest;
import com.example.demo.controllers.ItemControllerTest;
import com.example.demo.controllers.UserControllerTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        UserControllerTest.class,
        CartControllerTest.class,
        ItemControllerTest.class

})
public class SareetaApplicationTests {
    @Test
    public void contextLoads() {
    }

}
