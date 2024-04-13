package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private CartRepository cartRepository = mock(CartRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

     @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

     }

    @Test
    public void add_to_cart_happy_path() {

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("dani");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(1);

        User user = new User();
        Cart cart = new Cart();
        Item item = new Item();
        ArrayList itemsList = new ArrayList<>();
        cart.setItems(itemsList);
        user.setCart(cart);
        user.setUsername(modifyCartRequest.getUsername());
        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(user);

        item.setId(1L);
        item.setName("Round gadget");
        item.setPrice(BigDecimal.valueOf(10.99));
        Optional<Item> itemOptional = Optional.of(item);
        when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(itemOptional);

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

    }
}
