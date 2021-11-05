package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    Item item;
    List<Item> itemList;


    @Before
    public void setup(){
        itemController = new ItemController();

        TestUtils.injectObject(itemController, "itemRepository", itemRepository);

        BigDecimal total = new BigDecimal("4.99");
        item = new Item(3L, "Round Widget", total, "A widget that is round");
        itemList = new ArrayList<>();
        itemList.add(item);
    }

    @Test
    public void testGetItems(){
        when(itemRepository.findAll()).thenReturn(itemList);
        ResponseEntity<List<Item>> response = itemController.getItems();
        Assert.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testGetById(){
        when(itemRepository.findById(item.getId())).thenReturn(Optional.ofNullable(item));
        ResponseEntity<Item> response = itemController.getItemById(item.getId());
        Assert.assertEquals(200, response.getStatusCodeValue());

    }

    @Test
    public void testGetItemsByName(){
        Assert.assertNotNull(item);
        when(itemRepository.findByName(item.getName())).thenReturn(itemList);
        ResponseEntity<List<Item>> response = itemController.getItemsByName(item.getName());
        Assert.assertEquals(200, response.getStatusCodeValue());


    }





}
