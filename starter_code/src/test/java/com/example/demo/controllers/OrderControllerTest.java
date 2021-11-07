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
    private final UserRepository userRepository = mock(UserRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);
    List<Item> presetItems;

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);

        presetItems = new ArrayList<>();

        Item item = new Item();
        item.setId(0L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        presetItems.add(item);

        item = new Item();
        item.setId(1L);
        item.setName("Square Widget");
        item.setPrice(BigDecimal.valueOf(1.99));
        presetItems.add(item);
    }

    @Test
    public void submitOrderTest(){
        User user = new User();
        user.setUsername("testUser");
        user.setId(0);

        Cart cart = new Cart();
        cart.addItem(presetItems.get(0));
        cart.setId(0L);
        cart.setUser(user);
        user.setCart(cart);
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("unknownUser");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        List<UserOrder> userOrders = new ArrayList<>();

        response = orderController.submit("testUser");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(cart.getItems().size(), response.getBody().getItems().size());
        assertEquals(cart.getTotal(), response.getBody().getTotal());

        response.getBody().setId(0L);
        userOrders.add(response.getBody());

        cart = new Cart();
        cart.addItem(presetItems.get(0));
        user.setCart(cart);
        response = orderController.submit("testUser");

        response.getBody().setId(1L);
        userOrders.add(response.getBody());

        when(orderRepository.findByUser(user)).thenReturn(userOrders);

        ResponseEntity<List<UserOrder>> ordersResponse = orderController.getOrdersForUser("unknownUser");
        assertNotNull(ordersResponse);
        assertEquals(404, ordersResponse.getStatusCodeValue());

        ordersResponse = orderController.getOrdersForUser("testUser");
        assertNotNull(ordersResponse);
        assertEquals(200, ordersResponse.getStatusCodeValue());
        assertEquals(userOrders.size(), ordersResponse.getBody().size());
        assertEquals(userOrders.get(0).getId(), ordersResponse.getBody().get(0).getId());
    }

}