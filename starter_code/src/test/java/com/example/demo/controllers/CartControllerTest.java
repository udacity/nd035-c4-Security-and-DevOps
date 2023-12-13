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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class CartControllerTest {

    private CartController cartController;
    private final UserRepository userRepo = mock(UserRepository.class);
    private final ItemRepository itemRepo = mock(ItemRepository.class);
    private final CartRepository cartRepo = mock(CartRepository.class);

    @Autowired
    private MockMvc mockMvc;
    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
    }

    @Test
    public void addToCartSuccessTest(){

        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepo.findByUsername("Username")).thenReturn(user);
        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = newModifyCartRequest("Username", 1, 2);
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        Cart responseCart = responseEntity.getBody();

        assertNotNull(responseCart);

        List<Item> items = responseCart.getItems();
        assertNotNull(items);

        verify(cartRepo, times(1)).save(responseCart);
    }

    @Test
    public void addToCartFailedByInvalidUserNameTest(){

        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepo.findByUsername("Username")).thenReturn(user);
        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = newModifyCartRequest("Username1", 1, 2);
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());

    }

    @Test
    public void addToCartFailedByInvalidItemIDTest(){

        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepo.findByUsername("Username")).thenReturn(user);
        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = newModifyCartRequest("Username", 2, 2);
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());

    }

    @Test
    public void removeCartSuccessTest(){

        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepo.findByUsername("Username")).thenReturn(user);
        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = newModifyCartRequest("Username", 1, 2);
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

    }

    @Test
    public void removeCartFailedByUserNameTest(){

        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepo.findByUsername("Username")).thenReturn(user);
        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = newModifyCartRequest("Username", 2, 2);
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());

    }

    @Test
    public void removeCartFailedByItemIdTest(){

        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepo.findByUsername("Username")).thenReturn(user);
        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = newModifyCartRequest("Username2", 1, 2);
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());

    }
    public static User createUser(){
        User user = new User();
        user.setId(1);
        user.setUsername("Username");
        user.setPassword("Password");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(null);
        cart.setItems(new ArrayList<>());
        cart.setTotal(BigDecimal.valueOf(0.0));
        user.setCart(cart);

        return user;
    }

    public static Item createItem(){
        Item item = new Item();
        item.setId(1L);
        item.setName("New Item");
        item.setDescription("Decription item");
        item.setPrice(BigDecimal.valueOf(20.0));
        return item;
    }

    public static ModifyCartRequest newModifyCartRequest(String username, long itemId, int quantity){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(username);
        modifyCartRequest.setItemId(itemId);
        modifyCartRequest.setQuantity(quantity);
        return modifyCartRequest;
    }
}
