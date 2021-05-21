package com.example.demo.controllers;

import com.example.demo.utils.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    UserController userController;

    OrderController orderController;
    private UserRepository userRepository=mock(UserRepository.class);
    private CartRepository cartRepository=mock(CartRepository.class);
    private OrderRepository orderRepository= mock(OrderRepository.class);
    private BCryptPasswordEncoder encoder=mock(BCryptPasswordEncoder.class);
    @Before
    public void setup(){
        orderController = new OrderController();
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", encoder);
        TestUtils.injectObject(orderController, "userRepository", userRepository);
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);
    }
    private ResponseEntity<User> createNewUser(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        return userController.createUser(createUserRequest);
    }
    @Test
    public void submitTest(){
        when(encoder.encode("testPassword")).thenReturn("this is Hashed");
        ResponseEntity<User> response = createNewUser();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = new User();
        user.setUsername("test");
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        Cart cart = new Cart();
        //UserOrder order = new UserOrder();
        Item items = new Item();
        items.setDescription("bicycle");
        items.setId(0L);
        items.setName("bicycle");
        items.setPrice(new BigDecimal(880.00));
        //List<Item> itemList = new ArrayList<>();
        //itemList.add(items);
        cart.addItem(items);
        user.setCart(cart);
        ResponseEntity<UserOrder> userOrderResponseEntity = orderController.submit(user.getUsername());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }
    @Test
    public void submitTest_cannot_find_user(){
        ResponseEntity<UserOrder> userOrderResponseEntity = orderController.submit("Charles");
        assertNotNull(userOrderResponseEntity);
        assertEquals(404, userOrderResponseEntity.getStatusCodeValue());
    }
    @Test
    public void getOrdersForUser_success(){
        when(encoder.encode("testPassword")).thenReturn("this is Hashed");
        ResponseEntity<User> response = createNewUser();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = new User();
        user.setUsername("test");
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        UserOrder userOrder = new UserOrder();
        userOrder.setUser(user);
        Item items = new Item();
        items.setDescription("bicycle");
        items.setId(0L);
        items.setName("bicycle");
        items.setPrice(new BigDecimal(880.00));
        List<Item> itemList = new ArrayList<>();
        itemList.add(items);
        userOrder.setItems(itemList);
        userOrder.setTotal(new BigDecimal(880.00));
        userOrder.setId(0L);
        List<UserOrder> userOrderList = new ArrayList<>();
        userOrderList.add(userOrder);
        when(orderRepository.findByUser(user)).thenReturn(userOrderList);
        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser(user.getUsername());
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

    }

    @Test
    public void getOrdersForUser_cannot_find_user(){
        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("Charles");
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());

    }
}