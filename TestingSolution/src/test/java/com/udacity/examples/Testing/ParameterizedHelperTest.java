package com.udacity.examples.Testing;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ParameterizedHelperTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	private String inp;
	private String out;
	
	
	
public ParameterizedHelperTest(String inp, String out) {
		super();
		this.inp = inp;
		this.out = out;
	}


@Parameters
public static Collection initData() {
	String empNames[][] = {{"sareeta","sareeta"},{"john","jhn"}};
	return Arrays.asList(empNames);
	
}
	
	
	/**
	 * without parameters
	 */
	@Test
	public void verify_number_is_the_same(){
		assertEquals(inp, out);
	}
	
}
