package com.example.demo.controllers;

//Code assistance from https://knowledge.udacity.com/questions/467893
//help obtained from https://knowledge.udacity.com/questions/595874

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class ItemControllerTest {

    private ItemController itemController;

    private UserRepository userRepo = mock(UserRepository.class);

    private ItemRepository itemRepo = mock(ItemRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController,"itemRepository",itemRepo);
        Item item = new Item();
        item.setId(1L);
        item.setName("A Widget");
        BigDecimal price = BigDecimal.valueOf(2.99);
        item.setPrice(price);
        item.setDescription("A widget description");
        Item item2 = new Item();
        item2.setName("Square Widget");
        item2.setPrice(BigDecimal.valueOf(1.99));
        item2.setDescription("A widget that is square");
        when(itemRepo.findAll()).thenReturn(Collections.singletonList(item));
        when(itemRepo.findById(1L)).thenReturn(java.util.Optional.of(item));
        when(itemRepo.findByName("A Widget")).thenReturn(Collections.singletonList(item));
    }
    @Test
    public void testGetAllItems() {
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
    }

    @Test
    public void testGetItemsById() {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Item item = response.getBody();
        assertNotNull(item);
        assertEquals("A widget description", item.getDescription());
    }

    @Test
    public void testGetItemsByIdNotFound() {
        ResponseEntity<Item> response = itemController.getItemById(9L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testGetItemsByNamem() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("A Widget");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> item = response.getBody();
        assertNotNull(item);
        assertEquals("A Widget", item.get(0).getName());
    }
}



