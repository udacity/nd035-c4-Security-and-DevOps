package com.example.demo.controllers;

//help obtained from https://knowledge.udacity.com/questions/595874

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
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
    public void setUp() {

        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        Item item = new Item();
        item.setId(1L);
        item.setName("A Widget");
        BigDecimal price = BigDecimal.valueOf(2.99);
        item.setPrice(price);
        item.setDescription("A widget description");

        User user = new User();
        Cart cart = new Cart();
        user.setId(0);
        user.setUsername("Paul");
        user.setPassword("testPassword");
        user.setCart(cart);

        when(userRepo.findByUsername("Paul")).thenReturn(user);
        //when(UserOrder.createFromCart(cart)).thenReturn(userOrderMock);
        //when(orderRepo.save(userOrderMock)).thenReturn(userOrderMock);

    }

    @Test
    public void submitOrder() {
        ResponseEntity<UserOrder> userOrderResponseEntity = orderController.submit("Paul");
        assertNotNull(userOrderResponseEntity);
        assertEquals(200, userOrderResponseEntity.getStatusCodeValue());
        UserOrder userOrder = userOrderResponseEntity.getBody();
        assertNotNull(userOrder);
        assertEquals(1, userOrder.getItems().size());
    }
}
