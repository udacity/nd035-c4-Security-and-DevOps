package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.example.demo.ObjectsTest.userMock;
import static com.example.demo.ObjectsTest.userOrderListMock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Test
    public void submit_ok(){
       final String username = "user1";
       final User user = userMock();
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        final ResponseEntity<UserOrder> response =
                orderController.submit(username);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
        assertEquals(1, userOrder.getItems().get(0).getId());
    }

    @Test
    public void submit_not_found(){
        final String username = "user1";
        final ResponseEntity<UserOrder> response =
                orderController.submit(username);
        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void get_orders_for_user_ok(){
        final String username = "user1";
        final User user = userMock();
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(orderRepository.findByUser(any(User.class))).thenReturn(userOrderListMock());
        final ResponseEntity<List<UserOrder>> response =
                orderController.getOrdersForUser(username);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        List<UserOrder> userOrder = response.getBody();
        assertNotNull(userOrder);
        assertTrue(userOrder.size() > 0);
    }

    @Test
    public void get_orders_for_user_not_found(){
        final String username = "user1";
        final ResponseEntity<List<UserOrder>> response =
                orderController.getOrdersForUser(username);
        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
    }
}