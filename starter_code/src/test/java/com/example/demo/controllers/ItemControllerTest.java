package com.example.demo.controllers;

import com.example.demo.utils.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    UserController userController;
    ItemController itemController;
    OrderController orderController;
    private UserRepository userRepository=mock(UserRepository.class);
    private CartRepository cartRepository=mock(CartRepository.class);
    private OrderRepository orderRepository= mock(OrderRepository.class);
    private ItemRepository itemRepository= mock(ItemRepository.class);
    private BCryptPasswordEncoder encoder=mock(BCryptPasswordEncoder.class);
    @Before
    public void setup(){
        orderController = new OrderController();
        userController = new UserController();
        itemController = new ItemController();
        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", encoder);
        TestUtils.injectObject(orderController, "userRepository", userRepository);
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);
    }
    private ResponseEntity<User> createNewUser(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        return userController.createUser(createUserRequest);
    }

    @Test
    public void getItemsTest(){
        Item items = new Item();
        items.setDescription("bicycle");
        items.setId(0L);
        items.setName("bicycle");
        items.setPrice(new BigDecimal(880.00));
        items.setDescription("Mountain Bike");
        List<Item> itemList = new ArrayList<>();
        itemList.add(items);
        when(itemRepository.findAll()).thenReturn(itemList);
        ResponseEntity<List<Item>> responseEntity = itemController.getItems();
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }
    @Test
    public void getItemsTest_nothing_in_list(){
        Item items = new Item();
        List<Item> itemList = new ArrayList<>();
        itemList.add(items);
        when(itemRepository.findAll()).thenReturn(itemList);
        ResponseEntity<List<Item>> responseEntity = itemController.getItems();
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }
    @Test
    public void getItemByIdTest(){
        Item items = new Item();
        items.setDescription("bicycle");
        items.setId(0L);
        items.setName("bicycle");
        items.setPrice(new BigDecimal(880.00));
        List<Item> itemList = new ArrayList<>();
        itemList.add(items);
        when(itemRepository.findById(anyLong())).thenReturn(java.util.Optional.of(items));
        ResponseEntity<Item> responseEntity = itemController.getItemById(0L);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }
    @Test
    public void getItemByIdTest_cannot_find_id(){
        ResponseEntity<Item> responseEntity = itemController.getItemById(0L);
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }
    @Test
    public void getItemByNameTest(){
        Item items = new Item();
        items.setDescription("bicycle");
        items.setId(0L);
        items.setName("bicycle");
        items.setPrice(new BigDecimal(880.00));
        List<Item> itemList = new ArrayList<>();
        itemList.add(items);
        when(itemRepository.findByName(anyString())).thenReturn(itemList);
        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName(items.getName());
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }
    @Test
    public void getItemByIdName_cannot_find_name(){
        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("test");
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

}