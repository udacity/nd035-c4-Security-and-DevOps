package com.example.demo.authentication;


import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.security.UserDetailsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CartController.class)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CartAuthenticationNegativeTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    CartRepository cartRepository;

    @MockBean
    ItemRepository itemRepository;

    @Test
    public void unauthorizedGetItemById() throws Exception {
        mockMvc.perform(get("/api/cart/addToCart"))
                .andExpect(status().isForbidden()).andReturn();
    }
}
