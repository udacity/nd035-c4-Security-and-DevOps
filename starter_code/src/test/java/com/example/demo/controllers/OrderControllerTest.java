package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private final UserRepository userRepo = mock(UserRepository.class);
    private final OrderRepository orderRepo = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        com.example.demo.TestUtils.injectObjects(orderController, "userRepository", userRepo);
        com.example.demo.TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
    }

    @Test
    public void testSubmit() {
        String username = "Faruk";
        User user = new User();

        user.setUsername(username);
        user.setPassword("abcdefg");

        user.setId(0L);
        Item item = TestUtils.getItem01();
        Cart cart = new Cart();
        cart.setId(0L);

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);

        cart.setTotal(new BigDecimal("3.21"));
        cart.setUser(user);
        user.setCart(cart);
        cart.setUser(user);
        when(userRepo.findByUsername(username)).thenReturn(user);

        ResponseEntity<UserOrder> response =  orderController.submit(username);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        UserOrder retrievedUserOrder = response.getBody();
        assertNotNull(retrievedUserOrder);

        assertNotNull(retrievedUserOrder.getItems());
        assertNotNull(retrievedUserOrder.getTotal());

        assertNotNull(retrievedUserOrder.getUser());
    }

    @Test
    public void testSubmitNullUser() {
        String username = "Faruk";
        User user = new User();
        user.setPassword("abcdefg");
        user.setUsername(username);


        user.setId(0L);
        Item item = TestUtils.getItem01();
        Cart cart = new Cart();
        cart.setId(0L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        cart.setItems(itemList);
        cart.setTotal(new BigDecimal("3.21"));
        user.setCart(cart);

        cart.setUser(user);

        when(userRepo.findByUsername(username)).thenReturn(null);

        ResponseEntity<UserOrder> response =  orderController.submit(username);

        assertNotNull(response);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testGetOrdersForUser() {
        String username = "Faruk";
        User user = new User();
        user.setPassword("abcdefg");
        user.setUsername(username);

        user.setId(0L);

        Item item = TestUtils.getItem01();
        Cart cart = new Cart();

        cart.setId(0L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);
        cart.setTotal(new BigDecimal("3.21"));
        cart.setUser(user);
        user.setCart(cart);
        when(userRepo.findByUsername(username)).thenReturn(user);

        orderController.submit(username);

        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser(username);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        List<UserOrder> userOrders = responseEntity.getBody();
        assertNotNull(userOrders);
        assertEquals(0, userOrders.size());
    }

    @Test
    public void testGetOrdersForUserNullUser() {
        String username = "Faruk";
        User user = new User();
        user.setUsername(username);
        user.setPassword("abcdefg");
        user.setId(0L);
        Item item = TestUtils.getItem01();
        Cart cart = new Cart();
        cart.setId(0L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);
        cart.setTotal(new BigDecimal("3.21"));
        cart.setUser(user);
        user.setCart(cart);
        when(userRepo.findByUsername(username)).thenReturn(null);

        orderController.submit(username);

        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser(username);

        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }
}