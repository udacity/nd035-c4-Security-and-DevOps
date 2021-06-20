package com.example.demo;

import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.security.LoginRequest;
import com.example.demo.security.SecurityConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class AuthorizationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<CreateUserRequest> createUserRequestJson;

    @Autowired
    private JacksonTester<LoginRequest> loginRequestJson;

    @Autowired
    private UserRepository userRepository;

    private static final String USERNAME = "testUser";
    private static final String PASSWORD = "testPassword";

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    public void createUserAndLoginOk() throws Exception {
        //create user
        mvc.perform(
            post(new URI("/api/user/create"))
                .content(createUserRequestJson.write(getCreateUserRequest()).getJson())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value(USERNAME))
            .andExpect(jsonPath("$.password").doesNotExist());

        //login and get token
        LoginRequest loginRequest = new LoginRequest(USERNAME, PASSWORD);

        String token = mvc.perform(
            post(new URI("/login"))
                .content(loginRequestJson.write(loginRequest).getJson())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(header().exists(SecurityConstants.HEADER_STRING))
            .andReturn().getResponse().getHeader(SecurityConstants.HEADER_STRING);

        //get user info with token should succeed
        mvc.perform(
            get(new URI("/api/user/" + USERNAME))
                .header(SecurityConstants.HEADER_STRING, token))
            .andExpect(status().isOk());
    }

    @Test
    public void createUserAndLoginFalse() throws Exception {
        //create user
        mvc.perform(
            post(new URI("/api/user/create"))
                .content(createUserRequestJson.write(getCreateUserRequest()).getJson())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        //login with different password should fail
        LoginRequest loginRequest = new LoginRequest(USERNAME, PASSWORD + "_ABC");

        mvc.perform(
            post(new URI("/login"))
                .content(loginRequestJson.write(loginRequest).getJson())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void createUserTwiceFails() throws Exception {
        //create user
        mvc.perform(
            post(new URI("/api/user/create"))
                .content(createUserRequestJson.write(getCreateUserRequest()).getJson())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        mvc.perform(
            post(new URI("/api/user/create"))
                .content(createUserRequestJson.write(getCreateUserRequest()).getJson())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void passwordTooShort() throws Exception {
        CreateUserRequest createUserRequest = getCreateUserRequest();
        createUserRequest.setPassword("ABC");
        createUserRequest.setConfirmPassword("ABC");

        mvc.perform(
            post(new URI("/api/user/create"))
                .content(createUserRequestJson.write(createUserRequest).getJson())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void getUserForbidden() throws Exception {
        //get user without auth token should fail
        mvc.perform(get(new URI("/api/user/" + USERNAME)))
            .andExpect(status().isForbidden());
    }

    private CreateUserRequest getCreateUserRequest() {
        return new CreateUserRequest() {{
            setUsername(USERNAME);
            setPassword(PASSWORD);
            setConfirmPassword(PASSWORD);
        }};
    }
}
