package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private final ItemRepository itemRepository = mock(ItemRepository.class);
    List<Item> presetItems;

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

        presetItems = new ArrayList<>();

        Item item = new Item();
        item.setId(0L);
        item.setName("Round Widget");
        presetItems.add(item);

        item = new Item();
        item.setId(1L);
        item.setName("Square Widget");
        presetItems.add(item);
    }

    @Test
    public void testItemController(){
        when(itemRepository.findAll()).thenReturn(presetItems);
        when(itemRepository.findByName("Round Widget")).thenReturn(Collections.singletonList(presetItems.get(0)));
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.ofNullable(presetItems.get(0)));

        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());

        response = itemController.getItemsByName("Nonexistent name");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        response = itemController.getItemsByName("Round Widget");
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(0L, (long)response.getBody().get(0).getId());
        assertEquals("Round Widget", response.getBody().get(0).getName());

        ResponseEntity<Item> responseEntity = itemController.getItemById(0L);
        assertNotNull(responseEntity);
        assertNotNull(responseEntity.getBody());
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(0L, (long)responseEntity.getBody().getId());
    }



}