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
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private User user;
    private List<Item> items;
    private ModifyCartRequest modifyCartRequest;
    private final UserRepository userRpository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        cartController = new CartController();
        user = TestUtils.createUser();
        items = TestUtils.createItems();
        modifyCartRequest = new ModifyCartRequest();
        TestUtils.injectObjects(cartController, "userRepository", userRpository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }


    @Test
    public void verifyAddToCart() {
        when(userRpository.findByUsername("udacity")).thenReturn(user);
        when(itemRepository.findById(100L)).thenReturn(Optional.of(items.get(0)));

        modifyCartRequest.setUsername("udacity");
        modifyCartRequest.setItemId(100L);
        modifyCartRequest.setQuantity(5);

        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(modifyCartRequest);

        assertEquals(200, cartResponseEntity.getStatusCodeValue());

        Cart cart = cartResponseEntity.getBody();

        assertEquals(user, cart.getUser());
        assertEquals(7, cart.getItems().size());
        assertEquals(BigDecimal.valueOf(350), cart.getTotal());
    }

    @Test
    public void verifyAddToCartRetuns404WhenUserNotFound() {
        when(userRpository.findByUsername("doesnotexist")).thenReturn(null);

        modifyCartRequest.setUsername("doesnotexist");

        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(modifyCartRequest);

        assertEquals(404, cartResponseEntity.getStatusCodeValue());
        assertNull(cartResponseEntity.getBody());
    }

    @Test
    public void verifyAddToCartReturns404WhenItemNotFound() {
        when(userRpository.findByUsername("udacity")).thenReturn(user);
        when(itemRepository.findById(200L)).thenReturn(Optional.ofNullable(null));

        modifyCartRequest.setUsername("udacity");
        modifyCartRequest.setItemId(200L);

        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(modifyCartRequest);

        assertEquals(404, cartResponseEntity.getStatusCodeValue());
        assertNull(cartResponseEntity.getBody());
    }

    @Test
    public void verifyRemoveFromCart() {
        when(userRpository.findByUsername("udacity")).thenReturn(user);
        when(itemRepository.findById(100L)).thenReturn(Optional.of(items.get(0)));

        modifyCartRequest.setUsername("udacity");
        modifyCartRequest.setItemId(100L);
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> cartResponseEntity = cartController.removeFromcart(modifyCartRequest);

        assertEquals(200, cartResponseEntity.getStatusCodeValue());

        Cart cart = cartResponseEntity.getBody();

        assertEquals(1, cart.getItems().size());
        assertEquals(BigDecimal.valueOf(50L), cart.getTotal());
    }

    @Test
    public void verifyRemoveFromCartReturns404WhenUserNotFound() {
        when(userRpository.findByUsername("doesnotexist")).thenReturn(null);

        modifyCartRequest.setUsername("doesnotexist");

        ResponseEntity<Cart> cartResponseEntity = cartController.removeFromcart(modifyCartRequest);

        assertEquals(404, cartResponseEntity.getStatusCodeValue());
        assertNull(cartResponseEntity.getBody());
    }

    @Test
    public void verifyRemoveFromCartReturns404WhenItemNotFound() {
        when(userRpository.findByUsername("udacity")).thenReturn(user);
        when(itemRepository.findById(200L)).thenReturn(Optional.ofNullable(null));

        modifyCartRequest.setUsername("udacity");
        modifyCartRequest.setItemId(200L);

        ResponseEntity<Cart> cartResponseEntity = cartController.removeFromcart(modifyCartRequest);

        assertEquals(404, cartResponseEntity.getStatusCodeValue());
        assertNull(cartResponseEntity.getBody());
    }
}
