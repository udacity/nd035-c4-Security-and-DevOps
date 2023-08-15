package com.example.demo.controllers;

import com.example.demo.TestUtils;
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
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;

    private UserRepository userRepo = mock(UserRepository.class);

    private OrderRepository orderRepo = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);

        // test user
        User user = new User();
        user.setUsername("arvin");

        // create test items
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("testItem1");
        BigDecimal price1 = BigDecimal.valueOf(1.95);
        item1.setPrice(price1);

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("testItem2");
        BigDecimal price2 = BigDecimal.valueOf(10.05);
        item2.setPrice(price2);

        // create a cart
        Cart cart = new Cart();
        cart.addItem(item1);
        cart.addItem(item2);

        // set the cart to user
        user.setCart(cart);

        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);

        UserOrder order = UserOrder.createFromCart(cart);

        when(orderRepo.save(order)).thenReturn(order);

        when(orderRepo.findByUser(user)).thenReturn(Arrays.asList(order));

    }

    @Test
    public void submit_order_happy_path() throws Exception {
        ResponseEntity<UserOrder> response = orderController.submit("arvin");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder orderActual = response.getBody();
        assertNotNull(orderActual);
        assertEquals(2, orderActual.getItems().size());

    }

    @Test
    public void verify_getOrdersForUser() throws Exception {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("arvin");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> orders = response.getBody();
        assertNotNull(orders);
        assertEquals(1, orders.size());

        UserOrder order = orders.get(0);
        assertNotNull(order);
        assertEquals(12.00, order.getTotal().doubleValue(), 0.001);

    }
}
