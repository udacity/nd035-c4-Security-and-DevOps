package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

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
    public void get_all_items_happy_path() {
        ArrayList<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setName("Round gadget");
        items.add(item);
        when(itemRepository.findAll()).thenReturn(items);

        ResponseEntity response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void get_item_by_id_happy_path() {
        Long id = 1L;
        Item item = new Item();
        item.setName("Round gadget");
        when(itemRepository.findById(id)).thenReturn(java.util.Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(id);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void get_items_by_name_happy_path() {
        String name = "Round gadget";
        ArrayList<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setName("Round gadget");
        items.add(item);
        when(itemRepository.findByName(name)).thenReturn(items);

        ResponseEntity response = itemController.getItemsByName(name);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void get_by_id_error_path() {
        Long id = 1L;
        when(itemRepository.findById(id)).thenReturn(java.util.Optional.empty());

        ResponseEntity<Item> response = itemController.getItemById(id);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void search_by_name_error_path() {
        String name = "Round gadget";
        when(itemRepository.findByName(name)).thenReturn(new ArrayList<>());

        ResponseEntity response = itemController.getItemsByName(name);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void search_by_name_happy_path() {
        String name = "Round gadget";
        ArrayList<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setName("Round gadget");
        items.add(item);
        when(itemRepository.findByName(name)).thenReturn(items);

        ResponseEntity response = itemController.getItemsByName(name);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }
}
