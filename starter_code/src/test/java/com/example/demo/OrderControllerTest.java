package com.example.demo;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class OrderControllerTest {

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private OrderRepository orderRepositoryMock;

    @InjectMocks
    private OrderController orderController;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSubmitOrder() {
        //Setup
        User user = createUser("testUser");
        Cart cart = new Cart();
        cart.addItem(createTestItem(1L, "Item 1"));
        user.setCart(cart);

        when(userRepositoryMock.findByUsername("testUser")).thenReturn(user);
        when(orderRepositoryMock.save(Mockito.any(UserOrder.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        // Execute
        ResponseEntity<UserOrder> response = orderController.submit("testUser");

        // Verify
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(BigDecimal.valueOf(0.50f), response.getBody().getTotal());
    }

    @Test
    public void testSubmitOrderUserNotFound() {
        //Setup
        when(userRepositoryMock.findByUsername("testUser")).thenReturn(null);

        // Execute
        ResponseEntity<UserOrder> response = orderController.submit("testUser");

        // Verify
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetOrdersForUser() {
        //Setup
        User user = createUser("testUser");
        UserOrder order = new UserOrder();
        order.setId(1L);
        order.setUser(user);

        Item item = createTestItem(1L, "Item 1");
        List<Item> items = new ArrayList<>();
        items.add(item);

        order.setItems(items);

        List<UserOrder> orderList = new ArrayList<>();
        orderList.add(order);

        when(userRepositoryMock.findByUsername("testUser")).thenReturn(user);
        when(orderRepositoryMock.findByUser(user)).thenReturn(orderList);

        // Execute
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("testUser");

        // Verify
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(1, response.getBody().size());
        Assert.assertEquals(BigDecimal.valueOf(0.50f), response.getBody().get(0).getTotal());
    }

    @Test
    public void testGetOrdersForUserUserNotFound() {
        //Setup
        when(userRepositoryMock.findByUsername("testUser")).thenReturn(null);

        // Execute
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("testUser");

        // Verify
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private static User createUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setCart(new Cart());
        return user;
    }

    private static Item createTestItem(Long itemId,
                                       String name) {
        Item item = new Item();
        item.setId(itemId);
        item.setName(name);
        item.setDescription("test");
        item.setPrice(BigDecimal.valueOf(0.50f));
        return item;
    }
}

