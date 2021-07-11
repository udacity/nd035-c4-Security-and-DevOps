package com.example.demo.controller;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    OrderController orderController;

    private UserRepository userRepository=mock(UserRepository.class);

    private OrderRepository orderRepository=mock(OrderRepository.class);

    @Before
    public void setup() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);

        Item items= new Item();
        items.setId(1L);
        items.setDescription("abc");
        items.setName("item1");
        items.setPrice(BigDecimal.valueOf(20.0));

        List<Item> itemsList=new ArrayList<>();
        itemsList.add(items);

        Cart c = new Cart();
        c.setId(1L);
        c.setTotal(BigDecimal.valueOf(20));
        c.setItems(itemsList);

        User u = new User();
        u.setUsername("test");
        u.setId(2);
        u.setCart(c);
       when(userRepository.findByUsername(anyString())).thenReturn(u);
    }

    @Test
    public void submit_order() {

        ResponseEntity<UserOrder> userOrder= orderController.submit("test");
        assertEquals(200,userOrder.getStatusCodeValue());

    }

    @Test
    public void getOrdersForUser_test(){
        ResponseEntity<List<UserOrder>> ordersList =orderController.getOrdersForUser("test");
        assertEquals(200,ordersList.getStatusCodeValue());
    }


}
