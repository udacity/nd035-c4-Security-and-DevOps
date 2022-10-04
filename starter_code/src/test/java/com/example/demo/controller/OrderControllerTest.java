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
    public void setup() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void orderSubmitTest() {
        Cart cart = new Cart(0L, List.of(
                new Item(0L,
                        "Square Widget", new BigDecimal("1.99"),
                        "A widget that is square"),
                new Item(1L,
                        "Round Widget",
                        new BigDecimal("1.99"),
                        "A widget that is square")
        ),new User(0L,null,null,null),new BigDecimal("5"));
        when(userRepository.findByUsername("test")).thenReturn(new User(0,"test","testpassword",cart));

        ResponseEntity<UserOrder> response =  orderController.submit("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder order = response.getBody();
        assertEquals("Square Widget", order.getItems().get(0).getName());
        assertEquals("Round Widget", order.getItems().get(1).getName());
    }

}
