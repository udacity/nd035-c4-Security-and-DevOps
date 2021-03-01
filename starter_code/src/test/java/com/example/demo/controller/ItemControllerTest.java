package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        itemController = new ItemController();
        itemRepository = mock(ItemRepository.class);
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItemById_happy_path() {
        Item item = new Item();
        item.setId(1l);
        item.setName("Fake Item");

        when(itemRepository.findById(1l)).thenReturn(java.util.Optional.of(item));
        final ResponseEntity<Item> response = itemController.getItemById(1l);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Fake Item", response.getBody().getName());
    }

    @Test
    public void getItemById_crazy_path() {
        Item item = new Item();
        item.setId(1l);
        item.setName("Fake Item");

        when(itemRepository.findById(1l)).thenReturn(java.util.Optional.of(item));
        final ResponseEntity<Item> response = itemController.getItemById(99l);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getItemsByName_happy_path() {
        Item item = new Item();
        item.setId(1l);
        item.setName("Fake Item");
        List<Item> list = new ArrayList<Item>();
        list.add(item);

        when(itemRepository.findByName("Fake Item")).thenReturn(list);
        final ResponseEntity<List<Item>> response = itemController.getItemsByName("Fake Item");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Fake Item", response.getBody().get(0).getName());
    }
}
