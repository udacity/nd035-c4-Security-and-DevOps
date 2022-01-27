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

import static com.example.demo.TestUtils.createItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItemsTest(){
        ResponseEntity<List<Item>> response = itemController.getItems();
        List<Item> itemList =response.getBody();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(itemList);
    }

    @Test
    public void getItemByIdTest(){
        when(itemRepository.findById(1L)).thenReturn(Optional.of(createItem()));

        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        Item item= response.getBody();
        assertNotNull(item);

    }

    @Test
    public void getItemByNameTest(){

        List<Item> items = new ArrayList<>();
        items.add(createItem());
        when(itemRepository.findByName("Created Item")).thenReturn(items);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Created Item");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(items, response.getBody());
    }

}
