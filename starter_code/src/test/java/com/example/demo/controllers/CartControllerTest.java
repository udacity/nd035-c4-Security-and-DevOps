package com.example.demo.controllers;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;


public class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CartRepository cartRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addToCartSuccess() {
        User user = CreateDataController.createUser();
        Item item = CreateDataController.createItem();
        Cart cart = user.getCart();
        cart.setId(1L);
        cart.setTotal(BigDecimal.valueOf(0.0));
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        Mockito.when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("username");
        request.setItemId(1L);
        request.setQuantity(2);

        ResponseEntity<Cart> response = cartController.addTocart(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        Cart entity = response.getBody();
        Assert.assertNotNull(entity);
        Assert.assertNotNull(entity.getItems());
        Assert.assertNotNull(entity.getUser());

        Assert.assertEquals(user.getUsername(), entity.getUser().getUsername());

        Mockito.verify(cartRepository, Mockito.times(1)).save(entity);
    }

    @Test
    public void addToCartWithoutUser() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("username");
        request.setItemId(1L);
        request.setQuantity(2);

        ResponseEntity<Cart> response = cartController.addTocart(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void addToCartWithItems() {
        User user = CreateDataController.createUser();
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("username");
        request.setItemId(1L);
        request.setQuantity(2);

        ResponseEntity<Cart> response = cartController.addTocart(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void removeToCartSuccess() {
        User user = CreateDataController.createUser();
        Item item = CreateDataController.createItem();
        Cart cart = user.getCart();
        cart.setId(1L);
        cart.setTotal(BigDecimal.valueOf(0.0));
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        Mockito.when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("username");
        request.setItemId(1L);
        request.setQuantity(2);

        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        Cart entity = response.getBody();
        Assert.assertNotNull(entity);
        Assert.assertNotNull(entity.getItems());
        Assert.assertNotNull(entity.getUser());

        Assert.assertEquals(user.getUsername(), entity.getUser().getUsername());

        Mockito.verify(cartRepository, Mockito.times(1)).save(entity);
    }

    @Test
    public void removeToCartWithoutUser() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("username");
        request.setItemId(1L);
        request.setQuantity(2);

        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void removeToCartWithItems() {
        User user = CreateDataController.createUser();
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("username");
        request.setItemId(1L);
        request.setQuantity(2);

        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
