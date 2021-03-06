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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;

    // create a mock object of the repositories
    final private UserRepository userRepository = mock(UserRepository.class);
    final private CartRepository cartRepository = mock(CartRepository.class);
    final private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void verify_addToCart_and_removeFromCart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("naruto");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        // create a user
        User user = new User();
        user.setUsername("naruto");
        user.setPassword("ninja");
        user.setId(1L);

        // create an item
        Item item = new Item();
        item.setId(1L);
        item.setName("ramen");
        item.setPrice(BigDecimal.valueOf(11.50));

        // add created item to the collection of items
        List<Item> items = new ArrayList<>();
        items.add(item);

        // initialize cart for the user
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setId(1L);
        cart.setItems(items);
        cart.setTotal(BigDecimal.valueOf(11.50));

        //bi-directional relationship
        user.setCart(cart);

        when(userRepository.findByUsername("naruto")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));
        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(modifyCartRequest);

        assertNotNull(cartResponseEntity);
        assertEquals(200, cartResponseEntity.getStatusCodeValue());
        assertEquals(2, cartResponseEntity.getBody().getItems().size());

        ResponseEntity<Cart> cartResponseEntity2 = cartController.removeFromcart(modifyCartRequest);

        assertNotNull(cartResponseEntity2);
        assertEquals(200, cartResponseEntity2.getStatusCodeValue());
        assertEquals(1, cartResponseEntity2.getBody().getItems().size());
    }
}