package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.controllers.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void testGetItems() {
        Item item0 = getItem01();
        Item item1 = getItem02();

        List<Item> items = new ArrayList<>(2);
        items.add(item0);

        items.add(item1);
        when(itemRepository.findAll()).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> retrievedItems = response.getBody();
        assertNotNull(retrievedItems);

        assertEquals(2, retrievedItems.size());
        assertEquals(item0, retrievedItems.get(0));

        assertEquals(item1, retrievedItems.get(1));
    }

    @Test
    public void testGetItemById() {
        Item item0 = getItem01();
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(item0));

        ResponseEntity<Item> response = itemController.getItemById(0L);

        assertNotNull(response);

        assertEquals(200, response.getStatusCodeValue());
        Item retrievedItem = response.getBody();

        assertEquals(item0, retrievedItem);

        assertNotNull(retrievedItem);

        assertEquals(item0.getName(), retrievedItem.getName());
        assertEquals(item0.getId(), retrievedItem.getId());

        assertEquals(item0.getDescription(), retrievedItem.getDescription());
    }

    @Test
    public void testGetItemsByName() {
        Item item0 = getItem01();

        List<Item> items = new ArrayList<>(2);
        items.add(item0);


        when(itemRepository.findByName(WIDGET)).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItemsByName(WIDGET);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> retrievedItems = response.getBody();

        assertNotNull(retrievedItems);

        assertEquals(1, retrievedItems.size());
        assertEquals(item0, retrievedItems.get(0));
    }
}