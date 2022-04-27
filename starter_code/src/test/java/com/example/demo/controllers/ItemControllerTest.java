package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

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
}
