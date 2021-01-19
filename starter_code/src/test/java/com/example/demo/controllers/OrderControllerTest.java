package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private static final UserRepository userRepository = mock(UserRepository.class);
    private static final OrderRepository orderRepository = mock(OrderRepository.class);
    private static OrderController orderController;
    private static User user;

    @BeforeClass
    public static void setup() {
        orderController = new OrderController();
        user = TestUtils.createUser();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void verifySubmit() {
        when(userRepository.findByUsername("udacity")).thenReturn(user);
        ResponseEntity<UserOrder> userOrderResponseEntity = orderController.submit("udacity");

        assertEquals(200, userOrderResponseEntity.getStatusCodeValue());

        UserOrder userOrder = userOrderResponseEntity.getBody();

        assertEquals(TestUtils.createItems(), userOrder.getItems());
        assertEquals(user, userOrder.getUser());
        assertEquals(BigDecimal.valueOf(100L), userOrder.getTotal());
    }

    @Test
    public void verify404WhenUsernameNotFound() {
        when(userRepository.findByUsername("doesnotexist")).thenReturn(null);
        ResponseEntity<UserOrder> userOrderResponseEntity = orderController.submit("doesnotexist");
        assertEquals(404, userOrderResponseEntity.getStatusCodeValue());
    }

    @Test
    public void verifyGetOrdersForUser() {
        List<UserOrder> userOrders = TestUtils.createUserOrders();
        userOrders.get(0).setUser(user);

        when(userRepository.findByUsername("udacity")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(userOrders);

        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("udacity");

        assertEquals(200, responseEntity.getStatusCodeValue());

        List<UserOrder> orders = responseEntity.getBody();

        assertEquals(1, orders.size());

        UserOrder order = orders.get(0);

        assertEquals(order.getItems(), TestUtils.createItems());
        assertEquals(order.getTotal(), BigDecimal.valueOf(100L));
        assertEquals(order.getUser(), user);
    }

    @Test
    public void verify404WhenUserDoesNotExist() {
        when(userRepository.findByUsername("doesnotexist")).thenReturn(null);
        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("doesnotexist");
        assertEquals(404, responseEntity.getStatusCodeValue());
    }
}
