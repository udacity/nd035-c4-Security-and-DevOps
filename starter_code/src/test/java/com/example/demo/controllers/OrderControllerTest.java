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

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.TestUtils.createItem;
import static com.example.demo.TestUtils.createUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setup(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submitTest(){
        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);

        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("Username")).thenReturn(user);

        ResponseEntity<UserOrder> response =  orderController.submit("Username");

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

        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();

        cart.setUser(user);
        user.setCart(cart);

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);



        when(userRepository.findByUsername("Username")).thenReturn(null);

        ResponseEntity<UserOrder> response =  orderController.submit("Username");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void ordersByUserTest(){

        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);

        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("Username")).thenReturn(user);

        orderController.submit("Username");

        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("Username");

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        List<UserOrder> userOrders = responseEntity.getBody();
        assertNotNull(userOrders);
        assertEquals(0, userOrders.size());
    }

    @Test
    public void ordersByUserNullUser(){

        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();

        cart.setUser(user);
        user.setCart(cart);

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);


        when(userRepository.findByUsername("Username")).thenReturn(null);

        orderController.submit("Username");

        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("Username");

        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }
}

