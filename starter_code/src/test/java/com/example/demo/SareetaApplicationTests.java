package com.example.demo;

import org.junit.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SareetaApplicationTests {

	@AfterClass
	public static void afterClass() throws Exception {

	}

	@Before
	public void setUp() throws Exception {

	}

	@BeforeClass
	public static void beforeClass() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	@Order(1)
	public void contextLoads() {
	}

	@Test
	public void name() {
	}




}
