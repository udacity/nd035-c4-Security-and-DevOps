package com.example.demo.controllers;

import com.example.demo.TestUtils;
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
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    public OrderController orderController;

    final private UserRepository userRepository = mock(UserRepository.class);
    final private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setup() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void verify_submit_and_getOrdersForUser() {
        User user = generateUser();
        List<Item> items = Collections.singletonList(generateItem());

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(items);
        cart.setTotal(BigDecimal.valueOf(5));

        user.setCart(cart);

        UserOrder userOrder = new UserOrder();
        userOrder.setId(1L);
        userOrder.setUser(user);
        userOrder.setItems(items);
        userOrder.setTotal(BigDecimal.valueOf(5));

        // Test if submit works
        when(userRepository.findByUsername("naruto")).thenReturn(user);
        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(userOrder));
        ResponseEntity<UserOrder> failedResponse = orderController.submit("notNaruto");

        // not found
        assertNotNull(failedResponse);
        assertEquals(404, failedResponse.getStatusCodeValue());

        // Test if submit works
        when(userRepository.findByUsername("naruto")).thenReturn(user);
        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(userOrder));
        ResponseEntity<UserOrder> responseEntity = orderController.submit("naruto");

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(1, responseEntity.getBody().getItems().size());

        // Test if order history works
        when(userRepository.findByUsername("naruto")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(Collections.singletonList(userOrder));
        ResponseEntity<List<UserOrder>> responseEntity2 = orderController.getOrdersForUser("naruto");
        ResponseEntity<List<UserOrder>> failedResponseEntity2 = orderController.getOrdersForUser("notNaruo");

        assertNotNull(responseEntity2);
        assertEquals(200, responseEntity2.getStatusCodeValue());
        assertEquals(1, responseEntity2.getBody().size());
        assertEquals("ramen", responseEntity2.getBody().get(0).getItems().get(0).getName());

        // when checking the history of invalid user
        assertNotNull(failedResponseEntity2);
        assertEquals(404, failedResponseEntity2.getStatusCodeValue());

    }

    private User generateUser() {
        User user = new User();

        user.setUsername("naruto");
        user.setPassword("shipu");
        user.setId(1L);

        return user;
    }

    private Item generateItem() {
        Item item = new Item();

        item.setId(1L);
        item.setName("ramen");
        item.setPrice(BigDecimal.valueOf(5));

        return item;
    }
}