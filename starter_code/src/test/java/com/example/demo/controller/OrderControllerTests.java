package com.example.demo.controller;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderControllerTests {

    @Test
    public void testSubmitHappyPath() {
        // Arrange
        val user = getUser();
        val userRepositoryMock = mock(UserRepository.class);
        val orderRepositoryMock = mock(OrderRepository.class);
        when(userRepositoryMock.findByUsername(user.getUsername())).thenReturn(user);
        val orderController = new OrderController(userRepositoryMock, orderRepositoryMock);

        // Act
        val userOrderResponseEntity = orderController.submit(user.getUsername());

        // Assert
        assertEquals(HttpStatus.OK, userOrderResponseEntity.getStatusCode());
        val userOrder = userOrderResponseEntity.getBody();
        assertNotNull(userOrder);
        assertEquals(user.getCart().getItems(), userOrder.getItems());
        assertEquals(user, userOrder.getUser());

        verify(userRepositoryMock).findByUsername(user.getUsername());
        verify(orderRepositoryMock).save(any());
    }

    @Test
    public void testSubmitUnhappyPathUserNotFound() {
        // Arrange
        val username = "testUser";
        val userRepositoryMock = mock(UserRepository.class);
        val orderRepositoryMock = mock(OrderRepository.class);
        when(userRepositoryMock.findByUsername(username)).thenReturn(null);
        val orderController = new OrderController(userRepositoryMock, orderRepositoryMock);

        // Act
        val userOrderResponseEntity = orderController.submit(username);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, userOrderResponseEntity.getStatusCode());
        assertNull(userOrderResponseEntity.getBody());

        verify(userRepositoryMock).findByUsername(username);
        verify(orderRepositoryMock, never()).save(any());
    }

    @Test
    public void testGetOrdersForUserHappyPath() {
        // Arrange
        val user = getUser();
        val userOrders = Collections.singletonList(UserOrder.createFromCart(user.getCart()));
        val userRepositoryMock = mock(UserRepository.class);
        val orderRepositoryMock = mock(OrderRepository.class);
        when(userRepositoryMock.findByUsername(user.getUsername())).thenReturn(user);
        when(orderRepositoryMock.findByUser(user)).thenReturn(userOrders);
        val orderController = new OrderController(userRepositoryMock, orderRepositoryMock);

        // Act
        val userOrderResponseEntity = orderController.getOrdersForUser(user.getUsername());

        // Assert
        assertEquals(HttpStatus.OK, userOrderResponseEntity.getStatusCode());
        assertEquals(userOrders, userOrderResponseEntity.getBody());

        verify(userRepositoryMock).findByUsername(user.getUsername());
        verify(orderRepositoryMock).findByUser(user);
    }

    @Test
    public void testGetOrdersForUserUnhappyPathUserNotFound() {
        // Arrange
        val username = "testUser";
        val userRepositoryMock = mock(UserRepository.class);
        val orderRepositoryMock = mock(OrderRepository.class);
        when(userRepositoryMock.findByUsername(username)).thenReturn(null);
        val orderController = new OrderController(userRepositoryMock, orderRepositoryMock);

        // Act
        val userOrderResponseEntity = orderController.getOrdersForUser(username);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, userOrderResponseEntity.getStatusCode());
        assertNull(userOrderResponseEntity.getBody());

        verify(userRepositoryMock).findByUsername(username);
        verify(orderRepositoryMock, never()).findByUser(any());

    }

    private User getUser() {
        val items = Collections.singletonList(new Item());
        val cart = new Cart();
        cart.setItems(items);
        val user = new User();
        val username = "testUser";
        user.setUsername(username);
        user.setCart(cart);
        cart.setUser(user);
        return user;
    }

}
