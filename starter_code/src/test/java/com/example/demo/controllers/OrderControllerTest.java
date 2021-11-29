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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderControllerTest {

    private OrderController orderController;

    private final UserRepository userRepo = mock(UserRepository.class);

    private final OrderRepository orderRepo = mock(OrderRepository.class);

    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository", userRepo);
        TestUtils.injectObject(orderController, "orderRepository", orderRepo);
    }

    @Test
    public void submitOrderTest () throws Exception {
        User mockUser = createMockUser();
        UserOrder mockOrder = createUserOrder(mockUser);

        when(userRepo.findByUsername(mockUser.getUsername())).thenReturn(mockUser);
        when(orderRepo.save(mockOrder)).thenReturn(mockOrder);

        final ResponseEntity <UserOrder> responseEntity = orderController.submit(mockUser.getUsername());

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        UserOrder userOrder = responseEntity.getBody();
        assertNotNull(userOrder);
        assertEquals(mockOrder.getUser(), userOrder.getUser());
        assertEquals(mockOrder.getItems(), userOrder.getItems());
        assertEquals(mockOrder.getTotal(), userOrder.getTotal());

    }


    //Helper methods

    private User createMockUser (){
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("mockUserJeremy");
        mockUser.setCart(createMockCart(mockUser));
        return mockUser;
    }

    private Cart createMockCart (User user){
        Cart mockCart = new Cart();

        mockCart.setId(1L);
        mockCart.setItems(createMockItemList());
        mockCart.setUser(user);
        mockCart.setTotal(new BigDecimal("5.45"));
        return mockCart;
    }

    private List<Item> createMockItemList(){
        Item mockItem1 = createMockItem(1L,"Round Widget", new BigDecimal("2.30"), "A widget that is round" );
        Item mockItem2 = createMockItem(2L,"Square Widget", new BigDecimal("3.15"), "A widget that is square" );

        List<Item> mockItemList = new ArrayList<>();
        mockItemList.add(mockItem1);
        mockItemList.add(mockItem2);

        return mockItemList;

    }

    private Item createMockItem(Long id, String itemName, BigDecimal itemPrice, String itemDescription){

        Item newItem = new Item();
        newItem.setId(id);
        newItem.setName(itemName);
        newItem.setPrice(itemPrice);
        newItem.setDescription(itemDescription);
        return newItem;

    }

    private UserOrder createUserOrder(User user){
        UserOrder mockOrder = new UserOrder();
        mockOrder.setId(1L);
        mockOrder.setUser(user);
        mockOrder.setItems(user.getCart().getItems());
        mockOrder.setTotal(user.getCart().getTotal());

        return mockOrder;
    }


}
