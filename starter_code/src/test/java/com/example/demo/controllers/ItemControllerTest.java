package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private static ItemController itemController;
    private static List<Item> items;
    private static final ItemRepository itemRepository = mock(ItemRepository.class);

    @BeforeClass
    public static void setup() {
        itemController = new ItemController();
        items = TestUtils.createItems();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void verifyGetItems() {
        when(itemRepository.findAll()).thenReturn(items);

        ResponseEntity<List<Item>> itemResponseEntity = itemController.getItems();

        assertEquals(200, itemResponseEntity.getStatusCodeValue());

        List<Item> itemList = itemResponseEntity.getBody();

        assertEquals(items, itemList);
    }

    @Test
    public void verifyGetItemById() {
        Optional<Item> itemOptional = Optional.of(items.get(0));
        when(itemRepository.findById(100L)).thenReturn(itemOptional);

        ResponseEntity<Item> itemResponseEntity = itemController.getItemById(100L);

        assertEquals(200, itemResponseEntity.getStatusCodeValue());

        Item item = itemResponseEntity.getBody();

        assertEquals(item.getName(), "Widget");
        assertEquals(item.getDescription(), "A Square Widget");
        assertEquals(item.getPrice(), BigDecimal.valueOf(50L));
    }

    @Test
    public void verifyGetItemsByName() {
        when(itemRepository.findByName("Widget")).thenReturn(items.subList(0,1));
        ResponseEntity<List<Item>> itemResponseEntity = itemController.getItemsByName("Widget");

        assertEquals(200, itemResponseEntity.getStatusCodeValue());

        List<Item> itemList = itemResponseEntity.getBody();

        assertEquals(1, itemList.size());
        assertEquals(items.get(0), itemList.get(0));
    }

    @Test
    public void verifty404WhenItemNameDoesNotExist() {
        when(itemRepository.findByName("doesnotexist")).thenReturn(Collections.emptyList());
        ResponseEntity<List<Item>> itemResponseEntity = itemController.getItemsByName("doesnotexist");

        assertEquals(404, itemResponseEntity.getStatusCodeValue());
        assertNull(itemResponseEntity.getBody());
    }
}
