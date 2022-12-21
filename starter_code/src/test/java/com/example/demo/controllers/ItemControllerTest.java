package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemControllerTest {
    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllSuccess() {
        Item item = CreateDataController.createItem();
        List<Item> listItem = new ArrayList<>();
        listItem.add(item);

        Mockito.when(itemRepository.findAll()).thenReturn(listItem);

        ResponseEntity<List<Item>> response = itemController.getItems();
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Item> listEntity = response.getBody();
        Assert.assertNotNull(listEntity);
        Assert.assertEquals(1, listEntity.size());
    }

    @Test
    public void getItemByIdSuccess() {
        Item item = CreateDataController.createItem();
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));

        ResponseEntity<Item> response = itemController.getItemById(1L);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(item.getId(), response.getBody().getId());
    }

    @Test
    public void getItemByIdFailed() {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getItemsByNameSuccess() {
        Item item = CreateDataController.createItem();
        List<Item> listItem = new ArrayList<>();
        listItem.add(item);

        Mockito.when(itemRepository.findByName("Item")).thenReturn(listItem);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("Item");
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Item> listEntity = response.getBody();
        Assert.assertNotNull(listEntity);
        Assert.assertEquals(1, listEntity.size());
    }

    @Test
    public void getItemsByNameFailed() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Item");
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
