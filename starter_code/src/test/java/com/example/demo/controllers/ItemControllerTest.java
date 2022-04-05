package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void verify_getItems() {
        List<Item> items = TestUtils.createItems();
        when(itemRepository.findAll()).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> itemsBody = response.getBody();
        assertNotNull(itemsBody);
        assertEquals(items, itemsBody);
    }

    @Test
    public void verify_getItemById() {
        long id = 1L;
        Item item = TestUtils.createItem(id);
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(id);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item itemBody = response.getBody();
        assertNotNull(itemBody);
        assertEquals(Long.valueOf(1), itemBody.getId());
        assertEquals(item.getName(), itemBody.getName());
        assertEquals(item.getPrice(), itemBody.getPrice());
        assertEquals(item.getDescription(), itemBody.getDescription());
    }

    @Test
    public void verify_getItemByName() {
        long id = 1;
        List<Item> items = TestUtils.createItems();
        items.forEach(item -> item.setName("itemName"));
        when(itemRepository.findByName("itemName")).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("itemName");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> itemsInResponse = response.getBody();
        assertNotNull(itemsInResponse);
        assertEquals(items, itemsInResponse);
    }
}
