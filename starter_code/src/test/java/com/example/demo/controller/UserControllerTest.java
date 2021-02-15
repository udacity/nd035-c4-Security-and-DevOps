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
public class UserControllerTest {

    private static final String PASSWORD = "password";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<CreateUserRequest> json;

    @Test
    public void findByIdReturnsUser() throws Exception {
        createUser("findByIdReturnsUser_username");

        mvc.perform(
                get(new URI("/api/user/id/1")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();
    }

    @Test
    public void findByUsernameReturnsUser() throws Exception {
        final String username = "findByUsernameReturnsUser_username";
        createUser(username);

        mvc.perform(
                get(new URI("/api/user/" + username)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andReturn();
    }

    @Test
    public void createUserReturnsBadRequestWhenPasswordInvalid() throws Exception {
        mvc.perform(post(new URI("/api/user/create"))
                .content(json.write(TestUtil.createUserRequest("username", "p")).getJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

    }

    private void createUser(String username) throws Exception {
        mvc.perform(post(new URI("/api/user/create"))
                .content(json.write(TestUtil.createUserRequest(username, PASSWORD)).getJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8));
    }

}
