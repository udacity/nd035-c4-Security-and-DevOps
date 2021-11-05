package com.example.demo.controllers;

import com.example.demo.TestUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    CartController cartController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    ModifyCartRequest modifyCartRequest;
    User user;
    Item item;
    Cart cart;

    @Before
    public void setup(){
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);

        BigDecimal total = new BigDecimal("2.99");
        item = new Item(3L, "Round Widget", total, "A widget that is round");
        List<Item> items = new ArrayList<>();
        items.add(item);

        cart = new Cart(2L, items, total);

        modifyCartRequest = new ModifyCartRequest("test",3L, 5);
        user = new User(1L, "test", cart, "testPassword");

        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(user);
        when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(Optional.ofNullable(item));

    }

    @Test
    public void testAddToCart(){

        Assert.assertNotNull(user);
        Assert.assertNotNull(cart);
        Assert.assertNotNull(item);

        Assert.assertEquals(cart, user.getCart());

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        Assert.assertEquals(200, response.getStatusCodeValue());

    }

    @Test
    public void testRemoveFromCart(){
        Assert.assertNotNull(user);
        Assert.assertNotNull(cart);
        Assert.assertNotNull(item);

        Assert.assertEquals(cart, user.getCart());

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        Assert.assertEquals(200, response.getStatusCodeValue());
    }





}
