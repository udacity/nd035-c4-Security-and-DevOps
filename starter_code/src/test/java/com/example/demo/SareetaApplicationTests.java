package com.example.demo;

import com.example.demo.controller.CartControllerTest;
import com.example.demo.controller.ItemControllerTest;
import com.example.demo.controller.OrderControllerTest;
import com.example.demo.controller.UserControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		UserControllerTest.class,
		ItemControllerTest.class,
		CartControllerTest.class,
		OrderControllerTest.class
})
@SpringBootTest
public class SareetaApplicationTests {}
