package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);;
    private ItemRepository itemRepository = mock(ItemRepository.class);;

    @Before
    public void setup() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController,"userRepository", userRepository);
        TestUtils.injectObjects(cartController,"cartRepository", cartRepository);
        TestUtils.injectObjects(cartController,"itemRepository", itemRepository);
    }

    @Test
    public void addTocartTest() {
        when(userRepository.findByUsername("test")).thenReturn(new User(0,"test","testpassword",new Cart()));
        when(itemRepository.findById(0L)).thenReturn(Optional.of(new Item(0L, "Square Widget", new BigDecimal("1.99"), "A widget that is square")));
        ModifyCartRequest request = new ModifyCartRequest("test", 0L, 10);

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();
        System.out.println(cart.getItems().get(0).getName());
        assertEquals("Square Widget",cart.getItems().get(0).getName());
        assertEquals(new BigDecimal("1.99"),cart.getItems().get(0).getPrice());
    }

//    @Test
//    public void removeFromcartTest() {
//        when(userRepository.findByUsername("test")).thenReturn(
//                new User(0,
//                        "test",
//                        "testpassword",
//                        new Cart(0L, List.of(
//                                new Item(0L,
//                                "Square Widget", new BigDecimal("1.99"),
//                                "A widget that is square"),
//                                new Item(1L,
//                                        "Round Widget",
//                                        new BigDecimal("1.99"),
//                                        "A widget that is square")
//                        ),new User(),new BigDecimal("5"))));
//
//        when(itemRepository.findById(0L)).thenReturn(
//                Optional.of(
//                        new Item(0L,
//                                "Square Widget",
//                                new BigDecimal("1.99"),
//                                "A widget that is square")
//                        ));
//
//        ModifyCartRequest request = new ModifyCartRequest("test", 0L, 1);
//
//        ResponseEntity<Cart> response = cartController.removeFromcart(request);
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCodeValue());
//
//        Cart cart = response.getBody();
//        System.out.println(cart.getItems().size());
//    }
    
}
