package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private OrderRepository orderRepository;
    private UserRepository userRepository;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        orderController = new OrderController();
        orderRepository = mock(OrderRepository.class);
        userRepository = mock(UserRepository.class);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
    }

    @Test
    public void submit_happy_path() {

        Cart cart = new Cart();
        cart.setId(1L);

        User user = new User();
        user.setUsername("testUser");
        user.setId(1L);
        user.setCart(cart);
        cart.setUser(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("itemName");
        item.setPrice(BigDecimal.valueOf(1.99));

        cart.addItem(item);

        when(userRepository.findByUsername("testUser")).thenReturn(user);

        final ResponseEntity<UserOrder> response = orderController.submit("testUser");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("testUser", response.getBody().getUser().getUsername());
    }

    @Test
    public void getOrdersForUser_happy_path() {

        User user = new User();
        user.setUsername("testUser");
        user.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setName("itemName");
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(item);

        UserOrder order = new UserOrder();
        order.setId(1L);
        order.setUser(user);
        order.setItems(itemList);
        order.setTotal(BigDecimal.valueOf(9.99));

        List<UserOrder> orderList = new ArrayList<UserOrder>();
        orderList.add(order);

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(orderList);

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("testUser");
                assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("testUser", response.getBody().get(0).getUser().getUsername());

    }

    @Test
    public void getOrdersForUser_crazy_path() {

        User user = new User();
        user.setUsername("crazyUser");
        user.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setName("itemName");
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(item);

        UserOrder order = new UserOrder();
        order.setId(1L);
        order.setUser(user);
        order.setItems(itemList);
        order.setTotal(BigDecimal.valueOf(9.99));

        List<UserOrder> orderList = new ArrayList<UserOrder>();
        orderList.add(order);

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(orderList);

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("testUser");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotEquals("testUser", response.getBody().get(0).getUser().getUsername());

    }


}





