package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepo = mock(ItemRepository.class);


    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
    }

    @Test
    public void get_items(){
        Item item1 = new Item();
        Item item2 = new Item();
        Item item3 = new Item();
        List<Item> itemsInput = new ArrayList<Item>();
        itemsInput.add(item1);
        itemsInput.add(item2);
        itemsInput.add(item3);
        when(itemRepo.findAll()).thenReturn(itemsInput);

        ResponseEntity<List<Item>> response = itemController.getItems();

        List<Item> items = response.getBody();
        assertEquals(3, items.size() );
    }

    @Test
    public void get_item_by_id(){
        Item item = new Item();
        item.setId(Long.valueOf(0));
        item.setName("test-item");

        Optional<Item> optionalItem = Optional.of(item);
        when(itemRepo.findById(item.getId())).thenReturn(optionalItem);

        ResponseEntity<Item> response = itemController.getItemById(item.getId());
        Item itemResponse = response.getBody();

        assertEquals("test-item", itemResponse.getName());
    }

    @Test
    public void get_items_by_name_happy_path(){
        Item item1 = new Item();
        Item item2 = new Item();
        item1.setName("test-item");
        item2.setName("test-item");
        List<Item> itemsInput = new ArrayList<Item>();
        itemsInput.add(item1);
        itemsInput.add(item2);
        when(itemRepo.findByName("test-item")).thenReturn(itemsInput);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("test-item");

        List<Item> items = response.getBody();
        assertEquals(2, items.size() );
    }

    @Test
    public void get_items_by_name_unhappy_path(){
        List<Item> itemsInput = new ArrayList<Item>();
        when(itemRepo.findByName("test-item")).thenReturn(itemsInput);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("test-item");

        assertEquals(404, response.getStatusCodeValue());
    }
}
