package com.udacity.examples.Testing;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Test;

public class MockTest {

	/**
	 * What is a mock?
	 * How to mock?
	 * How to return multiple values using mock
	 */
	
	
	@Test
	public void returnMultipleValues() {
		Map mockList = mock(Map.class);
		when(mockList.get("sareeta Panda")).thenReturn(7).thenReturn(5);
		assertEquals(7, mockList.get("sareeta Panda"));
		assertEquals(5, mockList.get("sareeta Panda"));
	}

}
