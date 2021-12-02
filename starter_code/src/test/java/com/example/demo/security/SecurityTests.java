package com.example.demo.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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

    //Create user
    @Before
    public void setup () throws Exception{
        String userName = "jeremy";
        String password = "testPassword";
        String confirmPassword = "testPassword";


        String body = "{\"username\":\"" + userName + "\", \"password\":\"" + password + "\"," +
                " \"confirmPassword\":\"" + confirmPassword + "\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/create")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testGetOrderHistoryWithoutToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/order/history/jeremy"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetOrderHistoryWithToken() throws Exception{

        // try to access order history with token

        String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqZXJlbXlQIiwiZXhwIjoxNjM5MjQwNDI0fQ." +
                "8lRhu2x3vamsq8ww0_Wt7s_4TLxYzsLT70fPFxUOQlE8F0RkBDaAWpzrBiXBOfUpokN5dKXTqWZk1Hbe2ig22Q";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/order/history/jeremy")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }



}
