package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    public ItemController itemController;

    final private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void verify_getItems() {
        List<Item> items = collectionOfItems();
        when(itemRepository.findAll()).thenReturn(items);

        ResponseEntity<List<Item>> responseEntity = itemController.getItems();

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(2, responseEntity.getBody().size());
    }

    @Test
    public void verify_getItemById() {
        List<Item> items = collectionOfItems();
        when(itemRepository.findById(1L)).thenReturn(items.stream().findFirst());
        ResponseEntity<Item> responseEntity = itemController.getItemById(1L);
        ResponseEntity<Item> failedResponseEntity = itemController.getItemById(10L);

        // when the item exists
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("ramen", responseEntity.getBody().getName());

        // when the item does not exist
        assertNotNull(failedResponseEntity);
        assertEquals(404, failedResponseEntity.getStatusCodeValue());
    }

    @Test
    public void verify_getItemsByName() {
        List<Item> items = collectionOfItems();
        List<Item> salmon = Collections.singletonList(items.get(1));
        when(itemRepository.findByName("salmon")).thenReturn(salmon);
        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("salmon");
        ResponseEntity<List<Item>> failedResponseEntity = itemController.getItemsByName("salmonella");

        // when the item name exists
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(1, responseEntity.getBody().size());

        // when the item name does not exist
        assertNotNull(failedResponseEntity);
        assertEquals(404, failedResponseEntity.getStatusCodeValue());
    }

    // helper method for setting up all the items needed for the test
    private List<Item> collectionOfItems() {
        Item ramen = new Item();
        ramen.setId(1L);
        ramen.setName("ramen");
        ramen.setPrice(BigDecimal.valueOf(11.50));

        Item salmon = new Item();
        salmon.setId(2L);
        salmon.setName("salmon");
        salmon.setPrice(BigDecimal.valueOf(6));

        List<Item> items = new ArrayList<>();
        items.add(ramen);
        items.add(salmon);

        return items;
    }
}