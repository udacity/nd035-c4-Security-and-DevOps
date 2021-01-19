package com.example.demo;

// https://www.vogella.com/tutorials/JUnit/article.html#juniteclipse_testsuite

import com.example.demo.controllers.CartControllerTest;
import com.example.demo.controllers.ItemControllerTest;
import com.example.demo.controllers.OrderControllerTest;
import com.example.demo.controllers.UserControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        CartControllerTest.class,
        ItemControllerTest.class,
        OrderControllerTest.class,
        UserControllerTest.class
})
public class eCommerceTestSuite {
}
