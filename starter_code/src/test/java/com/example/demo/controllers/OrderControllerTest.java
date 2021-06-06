package com.example.demo.controllers;

//help obtained from https://knowledge.udacity.com/questions/595874

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
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class OrderControllerTest {

    private OrderController orderController;
    private OrderRepository orderRepo = mock(OrderRepository.class);
    private UserRepository userRepo = mock(UserRepository.class);
    private UserOrder userOrderMock = mock(UserOrder.class);

    @Before
    public void setUp(){

        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);

        User user = new User();
        user.setId(0L);
        user.setUsername("Paul");
        user.setPassword("abcdefg");

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        BigDecimal price = BigDecimal.valueOf(2.99);
        item.setPrice(price);
        item.setDescription("A widget that is round");
        List<Item> items = new ArrayList<>();
        items.add(item);


        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(items);
        cart.setUser(userRepo.findByUsername("Paul"));
        cart.setTotal(BigDecimal.valueOf(2.99));
        user.setCart(cart);
        when(userRepo.findByUsername("Paul")).thenReturn(user);
    }


    @Test
    public void submitOrder() {
        ResponseEntity<UserOrder> userOrderResponseEntity = orderController.submit("Paul");
        assertNotNull(userOrderResponseEntity);
        assertEquals(200, userOrderResponseEntity.getStatusCodeValue());
        UserOrder userOrder = userOrderResponseEntity.getBody();
        assertNotNull(userOrder);
        assertEquals(1, userOrder.getItems().size());
        //assertEquals("Paul", userOrder.getUser().getUsername());
    }

    @Test
    public void historyOrder() {
        ResponseEntity<List<UserOrder>> userOrderResponseEntity = orderController.getOrdersForUser("Paul");
        assertNotNull(userOrderResponseEntity);
        assertEquals(200, userOrderResponseEntity.getStatusCodeValue());
        List<UserOrder> userOrder = userOrderResponseEntity.getBody();
        assertNotNull(userOrder);
        //UserOrder uo = userOrder.get(0);
        //assertEquals(1, uo.getItems().size());
    }

    @Test
    public void historyNoOrderFoundForUser() {
        ResponseEntity<List<UserOrder>> userOrderResponseEntity = orderController.getOrdersForUser("Eddie");
        assertNotNull(userOrderResponseEntity);
        assertEquals(404, userOrderResponseEntity.getStatusCodeValue());
    }
}
