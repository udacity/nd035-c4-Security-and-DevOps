package com.example.demo.controllers;

import com.example.demo.Helper;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController,"itemRepository", itemRepository);

    }

    @Test
    public void get_items_happy_path_test(){

        Item itemOne = Helper.createItem();
        Item itemTwo = Helper.createItem();

        List<Item> itemList = new ArrayList<>();
        itemList.add(itemOne);
        itemList.add(itemTwo);

        when(itemRepository.findAll()).thenReturn(itemList);

        ResponseEntity<List<Item>> listResponseEntity = itemController.getItems();

        Assert.assertNotNull(listResponseEntity);
        Assert.assertEquals(HttpStatus.OK, listResponseEntity.getStatusCode());

        List<Item> items = listResponseEntity.getBody();
        Assert.assertNotNull(items);
        Assert.assertEquals(2, items.size());
    }


    @Test
    public void get_item_by_id_happy_path(){
        Item itemOne = Helper.createItem();
        when(itemRepository.findById(1L)).thenReturn(Optional.of(itemOne));

        ResponseEntity<Item> itemResponseEntity = itemController.getItemById(1L);
        Assert.assertNotNull(itemResponseEntity);

        Assert.assertEquals(HttpStatus.OK, itemResponseEntity.getStatusCode());
        Item item = itemResponseEntity.getBody();
        Assert.assertNotNull(item);
        Assert.assertEquals(itemOne, item);
    }

    @Test
    public void get_item_by_id_not_found_path(){
        ResponseEntity<Item> itemResponseEntity = itemController.getItemById(1L);
        Assert.assertNotNull(itemResponseEntity);
        Assert.assertEquals(HttpStatus.NOT_FOUND, itemResponseEntity.getStatusCode());
    }

    @Test
    public void get_items_by_name_happy_path(){
        Item itemOne = Helper.createItem();
        Item itemTwo = Helper.createItem();

        List<Item> itemList = new ArrayList<>();
        itemList.add(itemOne);
        itemList.add(itemTwo);

        when(itemRepository.findByName(itemOne.getName())).thenReturn(itemList);

        ResponseEntity<List<Item>> listResponseEntity = itemController.getItemsByName(itemOne.getName());

        Assert.assertNotNull(listResponseEntity);
        Assert.assertEquals(HttpStatus.OK, listResponseEntity.getStatusCode());
        Assert.assertEquals(2, listResponseEntity.getBody().size());
        Assert.assertNotNull(listResponseEntity.getBody());
    }

    @Test
    public void get_items_by_username_nt_found_path(){
        ResponseEntity<List<Item>> listResponseEntity = itemController.getItemsByName("teste");
        Assert.assertNotNull(listResponseEntity);

        Assert.assertEquals(HttpStatus.NOT_FOUND, listResponseEntity.getStatusCode());
    }
}
