package com.example.demo.controller;

import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureJsonTesters
public class OrderControllerTest {

    private static final String PASSWORD = "password";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<CreateUserRequest> json;

    @Test
    public void submitSuccessful() throws Exception {
        final String username = "submitSuccessful_username";
        createUser(username);

        mvc.perform(post(new URI("/api/order/submit/" + username)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.user.id").isNotEmpty())
                .andExpect(jsonPath("$.user.username").value(username));
    }

    @Test
    public void submitUnsuccessfulWhenUserNotFound() throws Exception {
        mvc.perform(post(new URI("/api/order/submit/" + "non-existing-user")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getOrdersForUserSuccessful() throws Exception {
        final String username = "getOrdersForUserSuccessful_username";
        createUser(username);

        mvc.perform(get(new URI("/api/order/history/" + username)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getOrdersForUserUnsuccessfulWhenUserNotFound() throws Exception {
        mvc.perform(get(new URI("/api/order/history/" + "non-existing-user")))
                .andExpect(status().isNotFound());
    }

    private void createUser(String username) throws Exception {
        mvc.perform(post(new URI("/api/user/create"))
                .content(json.write(TestUtil.createUserRequest(username, PASSWORD)).getJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8));
    }

}
