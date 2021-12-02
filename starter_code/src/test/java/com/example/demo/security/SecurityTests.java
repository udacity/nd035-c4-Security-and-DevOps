package com.example.demo.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUnauthenticatedUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cart")).andExpect(status().isForbidden());
    }



}
