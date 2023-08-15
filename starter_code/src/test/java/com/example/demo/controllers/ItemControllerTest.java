package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);

        // test items
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("testItem1");
        BigDecimal price1 = BigDecimal.valueOf(10.99);
        item1.setPrice(price1);
        item1.setDescription("test Description 1");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("testItem2");
        BigDecimal price2 = BigDecimal.valueOf(20.99);
        item2.setPrice(price2);
        item2.setDescription("test Description 2");

        // mocks behavior
        when(itemRepo.findAll()).thenReturn(Arrays.asList(item1, item2));
        when(itemRepo.findById(1l)).thenReturn(Optional.of(item1));
        when(itemRepo.findById(2l)).thenReturn(Optional.of(item2));
        when(itemRepo.findByName("testItem1")).thenReturn(Arrays.asList(item1));
        when(itemRepo.findByName("testItem2")).thenReturn(Arrays.asList(item2));

    }

    @Test
    public void verify_getItems() throws Exception {
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(2, items.size());

    }

    @Test
    public void verify_getItemById() throws Exception {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item item = response.getBody();
        assertNotNull(item);
        assertEquals("testItem1", item.getName());
        assertEquals(10.99, item.getPrice().doubleValue(), 0.001);

    }

    @Test
    public void verify_getItemByName() throws Exception {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("testItem2");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item item = response.getBody().get(0);
        assertNotNull(item);
        assertEquals("testItem2", item.getName());
        assertEquals("test Description 2", item.getDescription());
        assertEquals(20.99, item.getPrice().doubleValue(), 0.001);


    }
}
