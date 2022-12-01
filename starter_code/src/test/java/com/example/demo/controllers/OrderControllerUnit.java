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

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

@RunWith(SpringRunner.class)
public class OrderControllerUnit {

    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderController orderController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void given_validUsername_when_submit_then_returnSuccessAndUserOrder() {
        when(userRepository.findByUsername(any())).thenReturn(this.buildUser());
        ResponseEntity<UserOrder> response = this.orderController.submit("testuser1");

        assertNotNull(response);
        assertTrue(response.getStatusCode()
            .is2xxSuccessful());
    }

    @Test
    public void given_username_when_submit_butUserNull_return_HTTPNOTFOUND() {
        when(userRepository.findByUsername(any())).thenReturn(null);
        ResponseEntity<UserOrder> response = this.orderController.submit("testuser1");

        assertNotNull(response);
        assertTrue(response.getStatusCode()
            .is4xxClientError());
    }

    @Test
    public void given_validUsername_when_getOrdersForUser_then_success() {
        when(userRepository.findByUsername(any())).thenReturn(this.buildUser());
        when(orderRepository.findByUser(any())).thenReturn(this.buildOrders());

        ResponseEntity<List<UserOrder>> response = this.orderController.getOrdersForUser("usertest1");
        assertNotNull(response);
        assertTrue(response.getStatusCode()
            .is2xxSuccessful());
    }

    @Test
    public void given_username_when_getOrdersForUser_but_userNull_then_HTTPNOTFOUND() {
        when(userRepository.findByUsername(any())).thenReturn(null);
        when(orderRepository.findByUser(any())).thenReturn(null);

        ResponseEntity<List<UserOrder>> response = this.orderController.getOrdersForUser("testuser1");

        assertNotNull(response);
        assertTrue(response.getStatusCode()
            .is4xxClientError());
    }

    private User buildUser() {
        User user = new User();
        user.setId(1);
        user.setPassword("P@ssword!");
        user.setUsername("user1");
        Cart userCart = new Cart();
        userCart.setId((long) 1);
        userCart.setUser(user);
        userCart.setTotal(new BigDecimal(20));
        userCart.setItems(new ArrayList<Item>());
        user.setCart(userCart);
        return user;
    }

    private List<UserOrder> buildOrders() {
        List<UserOrder> orders = new ArrayList<UserOrder>();
        UserOrder userOrder = new UserOrder();
        userOrder.setId(1l);
        userOrder.setTotal(new BigDecimal(20));
        userOrder.setUser(new User());
        userOrder.setItems(new ArrayList<Item>());
        orders.add(userOrder);
        return orders;
    }

}
