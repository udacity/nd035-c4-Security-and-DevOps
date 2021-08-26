package com.example.demo.controllers;

import com.example.demo.Helper;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);


    @Before
    public void setup(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submit_order_happy_path_test(){
      User user = Helper.createUser();
      Item item = Helper.createItem();
      Cart cart = Helper.createcart();

      List<Item> itemList = new ArrayList<>();
      itemList.add(item);

      cart.setItems(itemList);
      cart.setUser(user);

      user.setCart(cart);

      when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<UserOrder> userOrderResponseEntity = orderController.submit(user.getUsername());

        Assert.assertNotNull(userOrderResponseEntity);
        Assert.assertEquals(HttpStatus.OK, userOrderResponseEntity.getStatusCode());
        UserOrder userOrderResponse= userOrderResponseEntity.getBody();
        Assert.assertEquals(cart.getItems(),userOrderResponse.getItems());
        Assert.assertEquals(user, userOrderResponse.getUser());
        Assert.assertEquals(BigDecimal.TEN, userOrderResponse.getTotal());
    }

    @Test
    public void submit_order_username_not_found(){
        when(userRepository.findByUsername(any())).thenReturn(null);
        ResponseEntity<UserOrder> userOrderResponseEntity = orderController.submit("username");
        Assert.assertNotNull(userOrderResponseEntity);
        Assert.assertEquals(HttpStatus.NOT_FOUND, userOrderResponseEntity.getStatusCode());
    }

    @Test
    public void get_history_order_for_user_happy_path(){
        User user = Helper.createUser();
        Item item = Helper.createItem();
        Cart cart = Helper.createcart();

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        cart.setItems(itemList);
        cart.setUser(user);

        user.setCart(cart);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<List<UserOrder>> listResponseEntity = orderController.getOrdersForUser(user.getUsername());

        Assert.assertNotNull(listResponseEntity);
        Assert.assertEquals(HttpStatus.OK, listResponseEntity.getStatusCode());

        List<UserOrder> userOrdersBody = listResponseEntity.getBody();

        Assert.assertNotNull(userOrdersBody);
    }

    @Test
    public void get_history_order_for_user_not_found(){
        when(userRepository.findByUsername(any())).thenReturn(null);
        ResponseEntity<List<UserOrder>> listResponseEntity = orderController.getOrdersForUser("teste");

        Assert.assertNotNull(listResponseEntity);
        Assert.assertEquals(HttpStatus.NOT_FOUND, listResponseEntity.getStatusCode());
    }


}
