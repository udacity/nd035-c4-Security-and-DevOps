package com.example.demo.controllers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RunWith(SpringRunner.class)
public class ItemControllerUnit {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemController itemController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void given_validName_when_getItemsByName_then_successAndItemsList() {
        when(this.itemRepository.findByName(any())).thenReturn(this.buildItemList());
        ResponseEntity<List<Item>> response = this.itemController.getItemsByName("AnyName");
        assertNotNull(response);
        assertTrue(response.getStatusCode()
            .is2xxSuccessful());
        assertTrue(response.getBody()
            .size() >= 1);

    }

    @Test
    public void given_itemName_when_getItemsByName_butNoItemsFound_then_HTTPNotFound() {
        when(this.itemRepository.findByName(any())).thenReturn(new ArrayList<Item>());
        ResponseEntity<List<Item>> response = this.itemController.getItemsByName("AnyName");
       
        assertNotNull(response);
        assertTrue(response.getStatusCode().is4xxClientError());
    }
    
    @Test
    public void given_itemName_when_getItemsByName_butNullItemList_then_HTTPNotFound() {
        when(this.itemRepository.findByName(any())).thenReturn(null);
        ResponseEntity<List<Item>> response = this.itemController.getItemsByName("AnyName");
       
        assertNotNull(response);
        assertTrue(response.getStatusCode().is4xxClientError());
    }
    
    private List<Item> buildItemList() {
        List<Item> items = new ArrayList<Item>();
        Item item1 = new Item();
        item1.setId((long) 1);
        item1.setName("Hair Comb");
        item1.setPrice(new BigDecimal(20));
        item1.setDescription("Hair Brush");
        items.add(item1);
        return items;
    }

}
