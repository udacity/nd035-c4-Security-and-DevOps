package com.example.demo.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
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
public class OrderControllerTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void init() throws InterruptedException {
    }

    @Test
    void testSubmit_success() throws Exception {
        Item item = createItemData("item1", new BigDecimal("123"));
        createUser("order_test1");
        createCart("order_test1", item.getId());

        mockMvc.perform(post("/api/order/submit/order_test1")).andExpect(status().isOk()).andReturn();
    }

    @Test
    void testSubmit_notFound() throws Exception {
        mockMvc.perform(post("/api/order/submit/order_test2")).andExpect(status().isNotFound()).andReturn();
    }

    @Test
    void testHistory_success() throws Exception {
        createOrder("order_test3");
        mockMvc.perform(get("/api/order/history/order_test3")).andExpect(status().isOk()).andReturn();
    }

    @Test
    void testHistory_notFound() throws Exception {
        mockMvc.perform(get("/api/order/history/order_test4")).andExpect(status().isNotFound()).andReturn();
    }

    private void createOrder(String userName) throws Exception {
        Item item = createItemData("item1", new BigDecimal("123"));
        createUser(userName);
        createCart(userName, item.getId());
        mockMvc.perform(post("/api/order/submit/" + userName)).andExpect(status().isOk()).andReturn();
    }

    private void createCart(String userName, Long itemId) throws Exception {
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(itemId);
        cartRequest.setQuantity(2);
        cartRequest.setUsername(userName);

        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = mapper.writeValueAsString(cartRequest);

        mockMvc.perform(post("/api/cart/addToCart").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
                .andExpect(status().isOk()).andReturn();
    }

    private void createUser(String userName) throws Exception {
        CreateUserRequest requestUser = new CreateUserRequest();
        requestUser.setUsername(userName);
        requestUser.setPassword("order_test1234");
        requestUser.setRePassword("order_test1234");

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
