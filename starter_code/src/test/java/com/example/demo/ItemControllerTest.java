package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    @Mock
    private ItemRepository itemRepositoryMock;

    @InjectMocks
    private ItemController itemController;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetItems() {
        //Setup
        List<Item> itemList = new ArrayList<>();
        itemList.add(createTestItem(1L, "Item 1"));
        itemList.add(createTestItem(2L, "Item 2"));

        when(itemRepositoryMock.findAll()).thenReturn(itemList);

        // Execute
        ResponseEntity<List<Item>> response = itemController.getItems();

        // Verify
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(2, response.getBody().size());
        Assert.assertEquals("Item 1", response.getBody().get(0).getName());
    }

    @Test
    public void testGetItemById() {
        //Setup
        Item item = createTestItem(1L, "Item 1");

        when(itemRepositoryMock.findById(1L)).thenReturn(Optional.of(item));

        // Execute
        ResponseEntity<Item> response = itemController.getItemById(1L);

        // Verify
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals("Item 1", response.getBody().getName());
    }

    @Test
    public void testGetItemByIdNotFound() {
        //Setup
        when(itemRepositoryMock.findById(any(Long.class))).thenReturn(Optional.empty());

        // Execute
        ResponseEntity<Item> response = itemController.getItemById(1L);

        // Verify
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetItemsByName() {
        //Setup
        List<Item> itemList = new ArrayList<>();
        itemList.add(createTestItem(1L, "Item 1"));
        itemList.add(createTestItem(2L, "Item 2"));
        when(itemRepositoryMock.findByName("Item 1")).thenReturn(itemList);

        // Execute
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Item 1");

        // Verify
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(2, response.getBody().size());
        Assert.assertEquals("Item 1", response.getBody().get(0).getName());
    }

    @Test
    public void testGetItemsByNameNotFound() {
        //Setup
        when(itemRepositoryMock.findByName(any(String.class))).thenReturn(null);

        // Execute
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Item 3");

        // Verify
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private static Item createTestItem(Long itemId,
                                       String name) {
        Item item = new Item();
        item.setId(itemId);
        item.setName(name);
        item.setDescription("test");
        item.setPrice(BigDecimal.valueOf(0.50f));
        return item;
    }
}