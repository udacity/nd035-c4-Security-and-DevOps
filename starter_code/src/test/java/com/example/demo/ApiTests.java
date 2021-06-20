package com.example.demo;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.security.AuthenticationService;
import com.example.demo.security.SecurityConstants;
import com.example.demo.security.UserRepositoryManager;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class ApiTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepositoryManager userRepositoryManager;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private JacksonTester<ModifyCartRequest> modifyCartRequestJson;

    private static final String USERNAME = "testUser";
    private static final String PASSWORD = "testPassword";

    private String token;
    private User user;

    @BeforeEach
    public void setup() {
        orderRepository.deleteAll();
        userRepository.deleteAll();
        user = userRepositoryManager.createUser(USERNAME, PASSWORD).get();
        authenticationService.authenticate(USERNAME, PASSWORD);
        token = SecurityConstants.TOKEN_PREFIX + authenticationService.createToken(USERNAME);
    }

    @Test
    public void getUserByName() throws Exception {
        mvc.perform(
            get(new URI("/api/user/" + USERNAME))
                .header(SecurityConstants.HEADER_STRING, token))
            .andExpect(status().isOk());
    }

    @Test
    public void getUserById() throws Exception {
        mvc.perform(
            get(new URI("/api/user/id/" + user.getId()))
                .header(SecurityConstants.HEADER_STRING, token))
            .andExpect(status().isOk());
    }

    @Test
    public void getAllItems() throws Exception {
        mvc.perform(
            get(new URI("/api/item"))
                .header(SecurityConstants.HEADER_STRING, token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getItemById() throws Exception {
        mvc.perform(
            get(new URI("/api/item/1"))
                .header(SecurityConstants.HEADER_STRING, token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").isString());
    }

    @Test
    public void getItemByName() throws Exception {
        mvc.perform(
            get(new URI("/api/item/name/Round%20Widget"))
                .header(SecurityConstants.HEADER_STRING, token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Round Widget"));
    }

    @Test
    public void getOrder() throws Exception {
        mvc.perform(
            get(new URI("/api/order/history/" + user.getUsername()))
                .header(SecurityConstants.HEADER_STRING, token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void submitOrder() throws Exception {
        mvc.perform(
            post(new URI("/api/order/submit/" + user.getUsername()))
                .header(SecurityConstants.HEADER_STRING, token))
            .andExpect(status().isOk());
    }

    @Test
    public void submitOrderForNonexistentUser() throws Exception {
        mvc.perform(
            post(new URI("/api/order/submit/" + "abc"))
                .header(SecurityConstants.HEADER_STRING, token))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void addAndRemoveCart() throws Exception {
        ModifyCartRequest request = new ModifyCartRequest(USERNAME, 1, 1);

        //add to cart
        mvc.perform(
            post(new URI("/api/cart/addToCart"))
                .content(modifyCartRequestJson.write(request).getJson())
                .contentType(MediaType.APPLICATION_JSON)
                .header(SecurityConstants.HEADER_STRING, token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.items").isArray())
            .andExpect(jsonPath("$.items").isNotEmpty());

        //remove from cart
        mvc.perform(
            post(new URI("/api/cart/removeFromCart"))
                .content(modifyCartRequestJson.write(request).getJson())
                .contentType(MediaType.APPLICATION_JSON)
                .header(SecurityConstants.HEADER_STRING, token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.items").isArray())
            .andExpect(jsonPath("$.items").isEmpty());
    }
}
