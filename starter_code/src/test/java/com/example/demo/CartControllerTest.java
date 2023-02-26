package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddToCart_Success() {
        // Setup
        Long itemId = 1L;
        int quantity = 2;

        User user = createTestUser();
        Item item = createTestItem(itemId);
        Cart cart = createCart(user);
        ModifyCartRequest request = createModifyCartRequest(user, quantity, itemId);

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(cartRepository.save(cart)).thenReturn(cart);

        // Execute
        ResponseEntity<Cart> response = cartController.addTocart(request);

        // Verify
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(cart, response.getBody());
        Assert.assertEquals(quantity, cart.getItems().size());
        Mockito.verify(cartRepository, Mockito.times(1)).save(cart);
    }

    @Test
    public void testAddToCart_UserNotFound() {
        // Setup
        Long itemId = 1L;
        int quantity = 2;

        User user = createTestUser();
        Cart cart = createCart(user);
        ModifyCartRequest request = createModifyCartRequest(user, quantity, itemId);

        // Execute
        ResponseEntity<Cart> response = cartController.addTocart(request);

        // Verify
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Mockito.verify(cartRepository, Mockito.times(0)).save(cart);
    }

    @Test
    public void testAddToCart_ItemNotFound() {
        // Setup
        Long itemId = 1L;
        int quantity = 2;

        User user = createTestUser();
        Cart cart = createCart(user);
        ModifyCartRequest request = createModifyCartRequest(user, quantity, itemId);

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        // Execute
        ResponseEntity<Cart> response = cartController.addTocart(request);

        // Verify
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Mockito.verify(cartRepository, Mockito.times(0)).save(cart);
    }

    @Test
    public void testRemoveFromCart_Success() {
        // Setup
        int quantity = 1;
        Long itemId = 1L;

        User user = createTestUser();
        Item item = createTestItem(itemId);
        Cart cart = createCart(user);
        ModifyCartRequest request = createModifyCartRequest(user, quantity, itemId);

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(cartRepository.save(cart)).thenReturn(cart);

        // Execute
        ResponseEntity<Cart> response = cartController.removeFromCart(request);

        // Verify
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(cart, response.getBody());
        Assert.assertEquals(0, cart.getItems().size());
        Mockito.verify(cartRepository, Mockito.times(1)).save(cart);
    }

    @Test
    public void testRemoveFromCart_UserNotFound() {
        // Setup
        int quantity = 1;
        Long itemId = 1L;

        User user = createTestUser();
        Cart cart = createCart(user);
        ModifyCartRequest request = createModifyCartRequest(user, quantity, itemId);

        // Execute
        ResponseEntity<Cart> response = cartController.removeFromCart(request);

        // Verify
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Mockito.verify(cartRepository, Mockito.times(0)).save(cart);
    }

    @Test
    public void testRemoveFromCart_ItemNotFound() {
        // Setup
        int quantity = 1;
        Long itemId = 1L;

        User user = createTestUser();
        Cart cart = createCart(user);
        ModifyCartRequest request = createModifyCartRequest(user, quantity, itemId);

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        // Execute
        ResponseEntity<Cart> response = cartController.removeFromCart(request);

        // Verify
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Mockito.verify(cartRepository, Mockito.times(0)).save(cart);
    }

    private static User createTestUser() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        return user;
    }

    private static ModifyCartRequest createModifyCartRequest(User user, int quantity, Long itemId) {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(user.getUsername());
        request.setItemId(itemId);
        request.setQuantity(quantity);
        return request;
    }

    private static Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);
        return cart;
    }

    private static Item createTestItem(Long itemId) {
        Item item = new Item();
        item.setId(itemId);
        item.setDescription("test");
        item.setPrice(BigDecimal.valueOf(0.50f));
        return item;
    }
}


