package com.example.demo.controllers;

import com.example.demo.TestUtils;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addToCartHappyPath() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("test");
        request.setItemId(1);
        request.setQuantity(1);

        Cart cart = new Cart();
        User user = TestUtils.createUser(1);
        user.setCart(cart);
        cart.setUser(user);
        Item item = TestUtils.createItem(1);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = cartController.addToCart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cartInResponse = response.getBody();
        assertNotNull(cartInResponse);
        assertEquals(user, cartInResponse.getUser());
        assertEquals(item, cartInResponse.getItems().get(0));
    }

    @Test
    public void addToCartUserNotFound() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("notFound");
        request.setItemId(1);
        request.setQuantity(1);

        Item item = TestUtils.createItem(1);

        when(userRepository.findByUsername("notFound")).thenReturn(null);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = cartController.addToCart(request);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void addToCartIemNotFound() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("test");
        request.setItemId(0);
        request.setQuantity(1);

        Cart cart = new Cart();
        User user = TestUtils.createUser(1);
        user.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(itemRepository.findById(0L)).thenReturn(Optional.empty());

        ResponseEntity<Cart> response = cartController.addToCart(request);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void verify_removeFromCart() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("test");
        request.setItemId(1);
        request.setQuantity(1);

        Cart cart = new Cart();
        List<Item> items = TestUtils.createItems();
        items.forEach(item -> cart.addItem(item));
        User user = TestUtils.createUser(1);
        user.setCart(cart);
        Item item = TestUtils.createItem(1);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = cartController.removeFromCart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cartInResponse = response.getBody();
        List<Item> itemsInResponse = cartInResponse.getItems();
        assertNotNull(cartInResponse);
        assertEquals(items.size() - 1, itemsInResponse.size());

        BigDecimal expectedTotal = TestUtils.createCart(1).getTotal().subtract(item.getPrice());
        assertEquals(expectedTotal, cartInResponse.getTotal());
    }
}
