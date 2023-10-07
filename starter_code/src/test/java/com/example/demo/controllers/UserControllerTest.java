package com.example.demo.controllers;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFindById() throws Exception {
        createUser("test1");
        MvcResult mvcResult = mockMvc.perform(get("/api/user/id/1")).andExpect(status().isOk()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        Object responseUser = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Object.class);
        assertNotNull(responseUser);
    }

    @Test
    public void testFindByUserName_existed() throws Exception {
        createUser("test2");
        MvcResult mvcResult = mockMvc.perform(get("/api/user/test2")).andExpect(status().isOk()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        Object responseUser = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Object.class);
        assertNotNull(responseUser);
    }

    @Test
    public void testFindByUserName_notFound() throws Exception {
        createUser("test3");
        mockMvc.perform(get("/api/user/notfound")).andExpect(status().isNotFound()).andReturn();
    }

    @Test
    public void testCreateUser() throws Exception {

        CreateUserRequest requestUser = new CreateUserRequest();
        requestUser.setUsername("test4");
        requestUser.setPassword("test123456789");
        requestUser.setRePassword("test123456789");

        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = mapper.writeValueAsString(requestUser);

        MvcResult mvcResult = mockMvc
                .perform(post("/api/user/create").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
                .andExpect(status().isOk()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        Object responseUser = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Object.class);
        assertNotNull(responseUser);
    }

    @Test
    public void testCreateUser_passwordNotMatching() throws Exception {

        CreateUserRequest requestUser = new CreateUserRequest();
        requestUser.setUsername("test4");
        requestUser.setPassword("test123456789");
        requestUser.setRePassword("test123456788");

        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = mapper.writeValueAsString(requestUser);

        mockMvc.perform(post("/api/user/create").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    public void testCreateUser_passwordInvalid() throws Exception {

        CreateUserRequest requestUser = new CreateUserRequest();
        requestUser.setUsername("test4");
        requestUser.setPassword("123");
        requestUser.setRePassword("123");

        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = mapper.writeValueAsString(requestUser);

        mockMvc.perform(post("/api/user/create").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
                .andExpect(status().isNotFound()).andReturn();
    }

    private User createUser(String userName) {
        User user = new User();
        user.setUsername(userName);
        user.setPassword("password");
        return userRepository.save(user);
    }
}
