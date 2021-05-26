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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTests {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private OrderController orderController;

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        initStubs();
    }

    private void initStubs() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");
        List<Item> items = Arrays.asList(item);

        User user = new User();
        user.setId(1L);
        user.setUsername("TestUser");
        user.setPassword("TestPassword");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(items);
        cart.setTotal(BigDecimal.valueOf(2.99));

        user.setCart(cart);
        when(userRepository.findByUsername("TestUser")).thenReturn(user);
    }

    @Test
    public void whenSubmitOrderShouldReturnUserOrderWhenSucceed() {
        ResponseEntity<UserOrder> response = orderController.submit("TestUser");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder order = response.getBody();
        assertNotNull(order);
        assertEquals(1, order.getItems().size());
    }

    @Test
    public void whenGetOrdersForUserShouldReturnUserOrderWhenSucceed() {
        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("TestUser");

        assertNotNull(ordersForUser);
        assertEquals(200, ordersForUser.getStatusCodeValue());
        assertNotNull(ordersForUser.getBody());
    }

    @Test
    public void whenSubmitOrderWithoutAnExistingUserShouldReturnNotFoundHttpStatusCode() {
        when(userRepository.findByUsername("InvalidUser")).thenReturn(null);

        ResponseEntity<UserOrder> response = orderController.submit("InvalidUser");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void whenGetOrdersWithoutAnExistingUserShouldReturnNotFoundHttpStatusCode() {
        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("InvalidUser");

        assertNotNull(ordersForUser);
        assertEquals(404, ordersForUser.getStatusCodeValue());
    }
}
