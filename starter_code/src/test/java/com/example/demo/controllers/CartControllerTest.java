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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private final UserRepository userRepo = mock(UserRepository.class);
    private final CartRepository cartRepo = mock(CartRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addTocart() {
        User user = new User();

        user.setUsername("Faruk");
        Item item = com.example.demo.controllers.TestUtils.getItem01();
        Cart cart = new Cart();
        cart.setId(0L);

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);

        cart.setTotal(new BigDecimal("3.21"));
        cart.setUser(user);
        user.setCart(cart);

        when(userRepo.findByUsername("Faruk")).thenReturn(user);

        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(item));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);

        request.setUsername("Faruk");

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart retrievedCart = response.getBody();

        assertNotNull(retrievedCart);
        assertEquals(java.util.Optional.of(0L), java.util.Optional.of(retrievedCart.getId()));
        List<Item> items = retrievedCart.getItems();

        assertNotNull(items);
        Item retrievedItem = items.get(0);

        assertEquals(2, items.size());
        assertNotNull(retrievedItem);
        assertEquals(item, retrievedItem);

        assertEquals(new BigDecimal("6.42"), retrievedCart.getTotal());
        assertEquals(user, retrievedCart.getUser());
    }

    @Test
    public void testAddToCartNullUser() {
        User user = new User();
        user.setUsername("Faruk");

        Item item = com.example.demo.controllers.TestUtils.getItem01();

        Cart cart = new Cart();
        cart.setId(0L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);

        cart.setTotal(new BigDecimal("3.21"));
        cart.setUser(user);
        user.setCart(cart);

        userRepo.save(user);
        cartRepo.save(cart);

        when(userRepo.findByUsername("Faruk")).thenReturn(null);
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(item));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername("Faruk");

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromcart() {
        User user = new User();
        user.setUsername("Faruk");

        Item item = com.example.demo.controllers.TestUtils.getItem01();
        Cart cart = new Cart();
        cart.setId(0L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        cart.setItems(itemList);
        cart.setTotal(new BigDecimal("3.21"));
        cart.setUser(user);
        user.setCart(cart);

        when(userRepo.findByUsername("Faruk")).thenReturn(user);
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(item));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);

        request.setQuantity(1);
        request.setUsername("Faruk");

        ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertNotNull(response);

        assertEquals(200, response.getStatusCodeValue());
        Cart retrievedCart = response.getBody();
        assertNotNull(retrievedCart);

        assertEquals(java.util.Optional.of(0L), java.util.Optional.of(retrievedCart.getId()));
        List<Item> items = retrievedCart.getItems();
        assertNotNull(items);

        assertEquals(0, items.size());
        assertEquals(new BigDecimal("0.00"), retrievedCart.getTotal());
        assertEquals(user, retrievedCart.getUser());
    }
}