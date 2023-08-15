package com.example.demo.controllers;

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
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp(){
        cartController = new CartController();

        // Try out using the included function in Mockito instead of our TestUtils class
        ReflectionTestUtils.setField(cartController, "userRepository", userRepo);
        ReflectionTestUtils.setField(cartController, "cartRepository", cartRepo);
        ReflectionTestUtils.setField(cartController, "itemRepository", itemRepo);

        Item item = new Item();
        item.setId(1L);
        item.setName("testItem1");
        BigDecimal price = BigDecimal.valueOf(1.99);
        item.setPrice(price);
        item.setDescription("test Description1");

        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));

        User user = new User();
        user.setUsername("arvin");
        Cart cart = new Cart();
        cart.addItem(item);
        user.setCart(cart);
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
        when(cartRepo.save(cart)).thenReturn(cart);
    }

    @Test
    public void add_to_cart_happy_path(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("arvin");
        request.setItemId(1L);
        request.setQuantity(2);

        ResponseEntity<Cart> responseActual = cartController.addToCart(request);
        assertNotNull(responseActual);
        assertEquals(200, responseActual.getStatusCodeValue());

        Cart cartActual = responseActual.getBody();
        assertNotNull(cartActual);
        assertEquals(3, cartActual.getItems().size());
        assertEquals(6, cartActual.getTotal().doubleValue(), 0.1);
    }

    @Test
    public void verifyRemoveFromCart(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("arvin");
        request.setItemId(1L);
        request.setQuantity(1);

        ResponseEntity<Cart> responseActual = cartController.removeFromCart(request);
        assertNotNull(responseActual);
        assertEquals(200, responseActual.getStatusCodeValue());

        Cart cartActual = responseActual.getBody();
        assertNotNull(cartActual);
        assertEquals(0, cartActual.getItems().size());
        assertEquals(0, cartActual.getTotal().doubleValue(), 0.01);
    }
}