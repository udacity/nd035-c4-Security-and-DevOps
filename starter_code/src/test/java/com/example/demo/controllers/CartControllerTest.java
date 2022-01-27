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

import java.util.List;
import java.util.Optional;

import static com.example.demo.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    private CartController cartController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository",itemRepository);
    }

    @Test
    public void addToCartNoUserError(){

        ModifyCartRequest modifyCartRequest = createModifyCartRequest("", 1, 1);
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);

        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void addToCartNoItemError(){

        when(userRepository.findByUsername("Username")).thenReturn(new User());
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ModifyCartRequest modifyCartRequest = createModifyCartRequest("Username", 1, 1);
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);

        assertNotNull(responseEntity);
        verify(itemRepository, times(1)).findById(1L);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void addToCartTest(){

        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("Username")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = createModifyCartRequest("Username", 1, 1);
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        Cart responseCart = responseEntity.getBody();

        assertNotNull(responseCart);

        List<Item> items = responseCart.getItems();
        assertNotNull(items);

        assertEquals("Username", responseCart.getUser().getUsername());

        verify(cartRepository, times(1)).save(responseCart);
    }

    @Test
    public void removeFromCartNoUserError(){

        ModifyCartRequest modifyCartRequest = createModifyCartRequest("", 1, 1);
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);

        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void removeFromCartNoItemError(){

        when(userRepository.findByUsername("Username")).thenReturn(new User());
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ModifyCartRequest modifyCartRequest = createModifyCartRequest("Username", 1, 1);
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);

        assertNotNull(responseEntity);
        verify(itemRepository, times(1)).findById(1L);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void removeFromCartTest(){

        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("Username")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = createModifyCartRequest("Username", 1, 1);
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        Cart responseCart = responseEntity.getBody();

        assertNotNull(responseCart);
        List<Item> items = responseCart.getItems();
        assertNotNull(items);
        assertEquals(0, items.size());
        assertEquals("Username", responseCart.getUser().getUsername());

        verify(cartRepository, times(1)).save(responseCart);

    }
}

