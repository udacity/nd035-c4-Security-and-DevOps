package com.example.demo.authentication;


import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.security.UserDetailsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class UserAuthenticationNegativeTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<CreateUserRequest> json;

    @MockBean
    UserRepository userRepository;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    CartRepository cartRepository;

    @Test
    public void createUserHappyPath() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("Sina");
        request.setPassword("testpassword");
        request.setConfirmPassword("testpassword");

        System.out.println(mockMvc.perform(
                        post("/api/user/create")
                                .content(json.write(request).getJson())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andReturn().getResponse().getContentAsString());
    }

    @Test
    public void unauthorizedFindByUserName() throws Exception {
        mockMvc.perform(get("/api/user/Sina"))
                .andExpect(status().isForbidden()).andReturn();
    }

    @Test
    public void unauthorizedFindById() throws Exception {
        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isForbidden()).andReturn();
    }
}
