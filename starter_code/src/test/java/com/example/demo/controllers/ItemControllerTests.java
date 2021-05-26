package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTests {
    private final ItemRepository itemRepository = mock(ItemRepository.class);
    private ItemController itemController;

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void whenFindAllItemsShouldReturnAListOfItems() {
        Item itemOne = new Item();
        itemOne.setId(1L);
        itemOne.setName("Round Widget");
        itemOne.setPrice(BigDecimal.valueOf(2.99));
        itemOne.setDescription("A widget that is round");

        Item itemTwo = new Item();
        itemTwo.setId(2L);
        itemTwo.setName("Square Widget");
        itemTwo.setPrice(BigDecimal.valueOf(1.99));
        itemTwo.setDescription("A widget that is square");

        List<Item> itemsExpected = Arrays.asList(itemOne, itemTwo);

        when(itemRepository.findAll()).thenReturn(itemsExpected);

        ResponseEntity<List<Item>> itemsResponse = itemController.getItems();
        assertNotNull(itemsResponse);
        assertEquals(200, itemsResponse.getStatusCodeValue());

        assertThat(itemsExpected, is(itemsResponse.getBody()));
    }

    @Test
    public void whenFindItemByIdShouldReturnAnItem() {
        Item itemOne = new Item();
        itemOne.setId(1L);
        itemOne.setName("Round Widget");
        itemOne.setPrice(BigDecimal.valueOf(2.99));
        itemOne.setDescription("A widget that is round");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(itemOne));

        ResponseEntity<Item> itemsResponse = itemController.getItemById(1L);
        assertNotNull(itemsResponse);
        assertEquals(200, itemsResponse.getStatusCodeValue());

        assertEquals(itemOne.getId(), itemsResponse.getBody().getId());
        assertEquals(itemOne.getName(), itemsResponse.getBody().getName());
        assertEquals(itemOne.getPrice(), itemsResponse.getBody().getPrice());
        assertEquals(itemOne.getDescription(), itemsResponse.getBody().getDescription());
    }

    @Test
    public void whenFindItemByNameShouldReturnAnItem() {
        Item itemOne = new Item();
        itemOne.setId(1L);
        itemOne.setName("Round Widget");
        itemOne.setPrice(BigDecimal.valueOf(2.99));
        itemOne.setDescription("A widget that is round");

        Item itemTwo = new Item();
        itemTwo.setId(2L);
        itemTwo.setName("Square Widget");
        itemTwo.setPrice(BigDecimal.valueOf(1.99));
        itemTwo.setDescription("A widget that is square");

        List<Item> itemsExpected = Arrays.asList(itemOne, itemTwo);

        when(itemRepository.findByName("Round Widget")).thenReturn(itemsExpected);

        ResponseEntity<List<Item>> itemsResponse = itemController.getItemsByName("Round Widget");
        assertNotNull(itemsResponse);
        assertEquals(200, itemsResponse.getStatusCodeValue());
        assertThat(itemsResponse.getBody(), hasItems(itemOne));
    }
}
