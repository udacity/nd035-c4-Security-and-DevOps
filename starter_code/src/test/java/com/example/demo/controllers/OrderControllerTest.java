package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private final UserRepository userRepo = mock(UserRepository.class);
    private final OrderRepository orderRepo = mock(OrderRepository.class);;

    @Before
    public void setup() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
    }

    @Test
    public void submitSuccessTest() {
        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepo.findByUsername("Username")).thenReturn(user);
        ResponseEntity<UserOrder> response = orderController.submit("Username");
        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void submitFailedTest() {
        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepo.findByUsername("Username")).thenReturn(user);
        ResponseEntity<UserOrder> response = orderController.submit("Username2");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUserSuccessTest() {
        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepo.findByUsername("Username")).thenReturn(user);
        when(orderRepo.findByUser(user)).thenReturn(getUserOrders());
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("Username");
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUserFailedTest() {
        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepo.findByUsername("Username")).thenReturn(user);
        when(orderRepo.findByUser(user)).thenReturn(getUserOrders());
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("Username2");
        assertEquals(404, response.getStatusCodeValue());
    }
    public static User createUser(){
        User user = new User();
        user.setId(1);
        user.setUsername("Username");
        user.setPassword("Password");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(null);
        cart.setItems(new ArrayList<>());
        cart.setTotal(BigDecimal.valueOf(0.0));
        user.setCart(cart);

        return user;
    }

    public static Item createItem(){
        Item item = new Item();
        item.setId(1L);
        item.setName("New Item");
        item.setDescription("Decription item");
        item.setPrice(BigDecimal.valueOf(20.0));
        return item;
    }

    private static List<UserOrder> getUserOrders() {
        UserOrder userOrder = UserOrder.createFromCart(createUser().getCart());
        return Lists.list(userOrder);
    }
}
