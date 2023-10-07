package com.example.demo.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class CartControllerTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSubmit_success() throws Exception {

        createUser("cart_test1");
        Item item = createItemData("item1", new BigDecimal("123"));

        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(item.getId());
        cartRequest.setQuantity(2);
        cartRequest.setUsername("cart_test1");

        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = mapper.writeValueAsString(cartRequest);

        mockMvc.perform(post("/api/cart/addToCart").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
                .andExpect(status().isOk()).andReturn();

    }

    @Test
    void testSubmit_usernameNotFound() throws Exception {

        createUser("cart_test2");
        Item item = createItemData("item1", new BigDecimal("123"));

        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(item.getId());
        cartRequest.setQuantity(2);
        cartRequest.setUsername("cart_test999");

        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = mapper.writeValueAsString(cartRequest);

        mockMvc.perform(post("/api/cart/addToCart").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
                .andExpect(status().isNotFound()).andReturn();

    }

    @Test
    void testSubmit_itemNotFound() throws Exception {

        createUser("cart_test3");

        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(999);
        cartRequest.setQuantity(2);
        cartRequest.setUsername("cart_test3");

        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = mapper.writeValueAsString(cartRequest);

        mockMvc.perform(post("/api/cart/addToCart").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    void testRemoveFromcart_success() throws Exception {

        createUser("cart_test4");
        Item item = createItemData("item1", new BigDecimal("123"));

        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(item.getId());
        cartRequest.setQuantity(2);
        cartRequest.setUsername("cart_test4");

        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = mapper.writeValueAsString(cartRequest);

        mockMvc.perform(post("/api/cart/removeFromCart").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
                .andExpect(status().isOk()).andReturn();

    }

    @Test
    void testRemoveFromcart_usernameNotFound() throws Exception {

        createUser("cart_test5");
        Item item = createItemData("item1", new BigDecimal("123"));

        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(item.getId());
        cartRequest.setQuantity(2);
        cartRequest.setUsername("cart_test999");

        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = mapper.writeValueAsString(cartRequest);

        mockMvc.perform(post("/api/cart/removeFromCart").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
                .andExpect(status().isNotFound()).andReturn();

    }

    @Test
    void testRemoveFromcart_itemNotFound() throws Exception {

        createUser("cart_test6");

        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(999);
        cartRequest.setQuantity(2);
        cartRequest.setUsername("cart_test6");

        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = mapper.writeValueAsString(cartRequest);

        mockMvc.perform(post("/api/cart/removeFromCart").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
                .andExpect(status().isNotFound()).andReturn();
    }

    private void createUser(String userName) throws Exception {
        CreateUserRequest requestUser = new CreateUserRequest();
        requestUser.setUsername(userName);
        requestUser.setPassword("cart_test1234");
        requestUser.setRePassword("cart_test1234");

        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = mapper.writeValueAsString(requestUser);

        mockMvc.perform(post("/api/user/create").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
                .andExpect(status().isOk()).andReturn();
    }

    private Item createItemData(String itemName, BigDecimal price) {
        Item item = new Item();
        item.setName(itemName);
        item.setPrice(price);
        item.setDescription("des " + itemName);

        return itemRepository.save(item);
    }
}
