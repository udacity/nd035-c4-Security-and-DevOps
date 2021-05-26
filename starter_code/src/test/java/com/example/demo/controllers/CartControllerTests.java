package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTests {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);
    private CartController cartController;

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
        initStub();
    }

    private void initStub() {
        User user = new User();
        user.setId(0);
        user.setUsername("TestUser_Cart");
        user.setPassword("TestPassword");
        user.setCart(new Cart());
        when(userRepository.findByUsername("TestUser_Cart")).thenReturn(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
    }

    @Test
    public void whenAddItemsToCartShouldReturnCartTotalWhenSucceed() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername("TestUser_Cart");
        modifyCartRequest.setQuantity(100);
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        Cart cart = responseEntity.getBody();

        assertTrue(BigDecimal.valueOf(299.00).compareTo(cart.getTotal()) == 0);
        assertThat(cart.getItems(), not(IsEmptyCollection.empty()));
    }

    @Test
    public void whenRemoveItemsFromCartShouldReturnCartTotalWhenSucceed() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername("TestUser_Cart");
        modifyCartRequest.setQuantity(100);
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername("TestUser_Cart");
        modifyCartRequest.setQuantity(99);
        responseEntity = cartController.removeFromcart(modifyCartRequest);
        Cart cart = responseEntity.getBody();

        assertTrue(BigDecimal.valueOf(2.99).compareTo(cart.getTotal()) == 0);
        assertThat(cart.getItems(), not(IsEmptyCollection.empty()));
    }

    @Test
    public void whenAddItemsToCartWithoutAnExistingUserShouldReturnNotFoundHttpStatusCode() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername("TestUser");
        modifyCartRequest.setQuantity(1);
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);

        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void whenAddItemsToCartWithoutAnExistingItemShouldReturnNotFoundHttpStatusCode() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(2L);
        modifyCartRequest.setUsername("TestUser_Cart");
        modifyCartRequest.setQuantity(1);
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);

        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void whenRemoveItemsFromCartWithoutAnExistingUserShouldReturnNotFoundHttpStatusCode() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername("TestUser");
        modifyCartRequest.setQuantity(1);
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);

        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void whenRemoveItemsFromCartWithoutAnExistingItemShouldReturnNotFoundHttpStatusCode() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(2L);
        modifyCartRequest.setUsername("TestUser_Cart");
        modifyCartRequest.setQuantity(1);
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);

        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

}
