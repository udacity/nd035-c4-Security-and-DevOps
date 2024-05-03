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
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submit_order_happy_path() {
        User user = new User();
        Cart cart = new Cart();
        user.setUsername("Beatricce");
        cart.setId(1L);
        ArrayList items = new ArrayList();
        cart.setItems(items);
        cart.setTotal(BigDecimal.valueOf(100.99));
        user.setCart(cart);
        when(userRepository.findByUsername("Beatricce")).thenReturn(user);

         ResponseEntity<UserOrder> response = orderController.submit("Beatricce");
            assertNotNull(response);
            assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void get_orders_for_user_happy_path() {
        User user = new User();
        Cart cart = new Cart();
        user.setUsername("Beatricce");
        cart.setId(1L);
        ArrayList items = new ArrayList<Item>();
        cart.setItems(items);
        cart.setTotal(BigDecimal.valueOf(250.99));
        user.setCart(cart);
        when(userRepository.findByUsername("Beatricce")).thenReturn(user);
        ResponseEntity<UserOrder> response = orderController.submit("Beatricce");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        ResponseEntity<List<UserOrder>> ordersForUserResponse = orderController.getOrdersForUser("Beatricce");
        assertNotNull(ordersForUserResponse);
        assertEquals(200, ordersForUserResponse.getStatusCodeValue());
    }

    @Test
    public void get_orders_for_user_not_found() {
        ResponseEntity<List<UserOrder>> ordersForUserResponse = orderController.getOrdersForUser("Beatricce");
        assertNotNull(ordersForUserResponse);
        assertEquals(404, ordersForUserResponse.getStatusCodeValue());
    }
}
