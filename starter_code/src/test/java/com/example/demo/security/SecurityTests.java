package com.example.demo.security;

import com.example.demo.model.requests.CreateUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MockHttpServletRequest request;

    CreateUserRequest userRequest;

    //Create user
    @Before
    public void init(){
        userRequest = new CreateUserRequest();
        userRequest.setUsername("jeremy");
        userRequest.setPassword("testPassword");
        userRequest.setConfirmPassword("testPassword");
    }

    @Test
    public void testGetOrderHistoryWithoutToken() throws Exception {

        //Check if user exists already
        if (userRequest.getUsername() == null){
            createUser();
        }

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/order/history/jeremy"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetOrderHistoryWithToken() throws Exception{

        //create user
        createUser();

        // try to access order history with token

        String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqZXJlbXkiLCJleHAiOjE2NDA0MTYzMTl9." +
                "pYl4HM8Ty3JWJF1N7TPzj2BqB9p19LYydisW6uA5G8ZwCGG9s5j-zrasxFC6XSq_4sayudIYR5j2NaJwjOlznA";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/order/history/jeremy")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

    //Helper method

    public void createUser() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/create")
                .content(objectMapper.writeValueAsString(userRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.username").exists());
    }

}
