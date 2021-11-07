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
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);
    List<Item> presetItems;

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        presetItems = new ArrayList<>();

        Item item = new Item();
        item.setId(0L);
        item.setName("Round Widget");
        item.setDescription("A widget that is round");
        item.setPrice(BigDecimal.valueOf(2.99));
        presetItems.add(item);

        item = new Item();
        item.setId(1L);
        item.setName("Square Widget");
        item.setDescription("A widget that is square");
        item.setPrice(BigDecimal.valueOf(1.99));
        presetItems.add(item);
    }

    @Test
    public void addToCart(){
        User user = new User();
        user.setUsername("testUser");
        user.setCart(new Cart());
        user.setId(0);
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.ofNullable(presetItems.get(0)));

        ModifyCartRequest request = new ModifyCartRequest();

        request.setUsername("wrongUser");
        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        request.setUsername("testUser");
        request.setItemId(1000);
        response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        request.setItemId(0);
        request.setQuantity(1);
        response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getItems().size());
        assertEquals( 0, (long)response.getBody().getItems().get(0).getId());
        assertEquals(presetItems.get(0).getPrice(), response.getBody().getTotal());

        response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().getItems().size());
        assertEquals(presetItems.get(0).getPrice().multiply(BigDecimal.valueOf(2)), response.getBody().getTotal());

    }

    @Test
    public void removeFromCart(){
        User user = new User();
        user.setUsername("testUser");
        user.setCart(new Cart());
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.ofNullable(presetItems.get(0)));

        ModifyCartRequest request = new ModifyCartRequest();

        request.setUsername("wrongUser");
        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        request.setUsername("testUser");
        request.setItemId(1000);
        response = cartController.removeFromcart(request);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        request.setItemId(0);
        request.setQuantity(1);
        response = cartController.removeFromcart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, response.getBody().getItems().size());
        assertEquals(BigDecimal.valueOf(0), response.getBody().getTotal());

        request.setQuantity(2);
        response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().getItems().size());
        assertEquals(presetItems.get(0).getPrice().multiply(BigDecimal.valueOf(2)), response.getBody().getTotal());

        request.setQuantity(1);
        response = cartController.removeFromcart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getItems().size());
        assertEquals(presetItems.get(0).getPrice(), response.getBody().getTotal());

    }

}