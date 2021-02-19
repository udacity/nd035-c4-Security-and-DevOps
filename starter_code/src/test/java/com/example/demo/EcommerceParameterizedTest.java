package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


import static org.junit.Assert.assertEquals;


@RunWith(Parameterized.class)
public class EcommerceParameterizedTest {

    private String input;
    private String output;

    public EcommerceParameterizedTest(String input, String output) {
        this.input = input;
        this.output = output;
    }

    @Parameterized.Parameters
    public static Collection initData(){
        String[][] empName = {{"sareeta", "sareeta"}, {"sareeta", "sareeta"}};
        return Arrays.asList(empName);

    }
    @Test
    public void verifyInputName(){
        assertEquals(input, output);

    }

}
