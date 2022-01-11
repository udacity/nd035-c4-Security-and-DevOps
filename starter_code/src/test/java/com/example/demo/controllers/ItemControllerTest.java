package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.gen5.api.Assertions.assertEquals;
import static org.junit.gen5.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    public static final String USERNAME = "test";
    private ItemController itemController;
    private final ItemRepository itemRepository = mock(ItemRepository.class);


    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);


    }

    @Test
    public void get_all_items(){
        Item item_0 = GetItemsUtils.getItem0();
        Item item_1 = GetItemsUtils.getItem1();
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(item_0);
        itemList.add(item_1);
        when(itemRepository.findAll()).thenReturn(itemList);
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> retrievedItems = response.getBody();
        assertNotNull(retrievedItems);
        assertEquals(2, retrievedItems.size());
        assertEquals(item_0, retrievedItems.get(0));
        assertEquals(item_1, retrievedItems.get(1));


    }

    @Test
    public void get_item_by_id(){
        Item item_0 = GetItemsUtils.getItem0();
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(item_0));
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Item retrievedItem = response.getBody();
        assertNotNull(retrievedItem);
        assertEquals("Round Widget",retrievedItem.getName());
        assertEquals("A widget that is round",retrievedItem.getDescription());
        assertEquals(new BigDecimal("2.99"), retrievedItem.getPrice());

    }

    @Test
    public void get_item_by_username(){
        Item item_0 = GetItemsUtils.getItem0();
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(item_0));
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Item retrievedItem = response.getBody();
        assertNotNull(retrievedItem);
        assertEquals("Round Widget",retrievedItem.getName());
        assertEquals("A widget that is round",retrievedItem.getDescription());
        assertEquals(new BigDecimal("2.99"), retrievedItem.getPrice());

    }

}
