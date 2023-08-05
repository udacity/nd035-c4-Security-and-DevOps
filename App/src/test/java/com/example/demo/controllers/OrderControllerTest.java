package com.example.demo.controllers;


import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderControllerTest {

    OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);


    @Before
    public void setup(){
        orderController = new OrderController();

        TestUtils.injectObjects(orderController,"userRepository",userRepository);
        TestUtils.injectObjects(orderController,"orderRepository",orderRepository);

        Item item = new Item();
        item.setId(1L);
        item.setName("New Item");
        item.setDescription("Test Description");
        item.setPrice(BigDecimal.valueOf(5));

        Cart cart = new Cart();
        cart.setId(1L);
        cart.addItem(item);
        cart.addItem(item);
        cart.setTotal(BigDecimal.valueOf(10));


        User user = new User();
        user.setUsername("UsernameOrderTest");
        user.setPassword("password");
        user.setCart(cart);
        cart.setUser(user);

        when(userRepository.findByUsername("UsernameOrderTest")).thenReturn(user);
    }

    @Test
    @Transactional
    public void submitTest(){

        ResponseEntity<UserOrder> order = orderController.submit( "UsernameOrderTest");

        Assert.assertNotNull(order);
        Assert.assertEquals(200,order.getStatusCodeValue());

        Assert.assertEquals("UsernameOrderTest", order.getBody().getUser().getUsername());
        Assert.assertEquals(2,order.getBody().getItems().size());
        Assert.assertEquals(BigDecimal.valueOf(10),order.getBody().getTotal());

    }

    @Test
    public void getOrderForUserTest(){

        UserOrder userorder = orderController.submit("UsernameOrderTest").getBody();
        when(orderRepository.findByUser(userRepository.findByUsername("UsernameOrderTest"))).thenReturn(Arrays.asList(userorder));
        ResponseEntity<List<UserOrder>> userOrders = orderController.getOrdersForUser("UsernameOrderTest");

        Assert.assertNotNull(userOrders);
        Assert.assertEquals(200,userOrders.getStatusCodeValue());
        Assert.assertEquals(1,userOrders.getBody().size());

    }

}
