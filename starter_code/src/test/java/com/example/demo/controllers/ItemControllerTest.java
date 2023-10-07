package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ItemControllerTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        itemRepository.deleteAll();
    }

    @Test
    public void testGetItems() throws Exception {

        createItemData("test1", new BigDecimal("123"));
        createItemData("test2", new BigDecimal("234"));
        MvcResult mvcResult = mockMvc.perform(get("/api/item")).andExpect(status().isOk()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        List<Item> itemList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<Item>>() {
                });

        assertEquals(itemList.size(), 2);
    }

    @Test
    public void testGetItemById() throws Exception {

        Item item = createItemData("test3", new BigDecimal("345"));
        MvcResult mvcResult = mockMvc.perform(get("/api/item/" + item.getId())).andExpect(status().isOk()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        Object itemRes = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Object.class);
        assertNotNull(itemRes);
    }

    @Test
    public void testGetItemsByName_existed() throws Exception {

        createItemData("test4", new BigDecimal("234"));
        MvcResult mvcResult = mockMvc.perform(get("/api/item/name/test4")).andExpect(status().isOk()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        Object itemRes = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Object.class);
        assertNotNull(itemRes);
    }

    @Test
    public void testGetItemsByName_notfound() throws Exception {

        createItemData("test5", new BigDecimal("234"));
        mockMvc.perform(get("/api/item/name/test4")).andExpect(status().isNotFound()).andReturn();
    }

    Item createItemData(String itemName, BigDecimal price) {
        Item item = new Item();
        item.setName(itemName);
        item.setPrice(price);
        item.setDescription("des " + itemName);

        return itemRepository.save(item);
    }
}
