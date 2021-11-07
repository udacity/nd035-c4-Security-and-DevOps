package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;


    private final UserRepository userRepository = mock(UserRepository.class);

    private final OrderRepository orderRepository = mock(OrderRepository.class);

    User user;

    UserOrder userOrder;

    @Before
    public void setup(){
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository", userRepository);
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);

        BigDecimal total = new BigDecimal("2.99");
        Item item = new Item(3L, "Round Widget", total, "A widget that is round");
        List<Item> items = new ArrayList<>();
        items.add(item);

        Cart cart = new Cart(2L, items, total);

        user = new User(1L, "test", cart, "testPassword");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        userOrder = new UserOrder(4L, items, user, total);
    }

    @Test
    public void testSubmitOrderInOrderController(){

        ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());
        Assert.assertEquals(200, response.getStatusCodeValue());

        user = new User();
        //user
        ResponseEntity<UserOrder> newResponse = orderController.submit(user.getUsername());
        Assert.assertEquals(404, newResponse.getStatusCodeValue());
    }

    @Test
    public void testGetOrdersForUserInOrderController(){
        List<UserOrder> userOrderList = new ArrayList<>();
        userOrderList.add(userOrder);

        Assert.assertNotNull(user);

        when(orderRepository.findByUser(user)).thenReturn(userOrderList);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());
        Assert.assertEquals(200, response.getStatusCodeValue());


    }


}
