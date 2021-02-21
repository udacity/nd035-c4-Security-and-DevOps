package com.example.demo.security;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.net.URI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@Transactional
public class UserAuthTest {
    @Autowired
    private MockMvc mvc;

    @Before
    public void setup() throws Exception {

    }

    @Test
    public void given_correct_details_able_to_create_user_and_login() throws Exception {

        String json = "{\"username\": \"Suresh\",\"password\":\"pass12345678\",\"confirmPassword\": \"pass12345678\"}";

        ResultActions registrationResult = mvc.perform(
                post(new URI("/api/user/create"))
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());


        //Login with right password
        json = "{\"username\": \"Suresh\",\"password\" : \"pass12345678\"}";
        ResultActions loginResult = mvc.perform(
                post(new URI("/login"))
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        String accessToken = loginResult.andReturn().getResponse().getHeader("Authorization");

        Assert.assertNotNull(accessToken);


        //Get user details with access token - It should work
        mvc.perform(
                get(new URI("/api/user/Suresh"))
                        .header("Authorization", "Bearer " + accessToken)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

    }

    @Test
    public void given_user_cannot_access_apis_without_bearer_jwt() throws Exception {
        // Access user profile without access token - negative test
        String json = "{\"username\": \"Suresh\",\"password\" : \"pass12345678\"}";
        mvc.perform(
                get(new URI("/api/user/Suresh"))
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());

    }

    @Test
    public void any_user_can_access_inventory_without_being_authenticated() throws Exception {
        // Access user profile without access token - negative test
        mvc.perform(
                get(new URI("/api/item"))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

    }


    @Test
    public void given_user_can_not_access_purchases_history_without_being_authenticated() throws Exception {

        mvc.perform(
                get(new URI("/history/Suresh"))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());

    }

}
