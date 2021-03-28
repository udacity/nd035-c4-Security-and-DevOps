package com.example.demo.controller;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartControllerTests {

    @Test
    public void testAddToCartHappyPath() {
        // Arrange
        val modifyCartRequest = getModifyCartRequest();
        val requestItemPrice = BigDecimal.TEN;

        val userRepositoryMock = mock(UserRepository.class);
        val foundUser = new User();
        foundUser.setCart(new Cart());
        when(userRepositoryMock.findByUsername(modifyCartRequest.getUsername())).thenReturn(foundUser);

        val itemRepositoryMock = mock(ItemRepository.class);
        val foundItem = new Item();
        foundItem.setId(modifyCartRequest.getItemId());
        foundItem.setPrice(requestItemPrice);
        when(itemRepositoryMock.findById(modifyCartRequest.getItemId())).thenReturn(Optional.of(foundItem));

        val cartRepositoryMock = mock(CartRepository.class);

        val cartController = new CartController(userRepositoryMock, cartRepositoryMock, itemRepositoryMock);

        // Act
        val cartResponseEntity = cartController.addToCart(modifyCartRequest);

        // Assert
        val responseCart = cartResponseEntity.getBody();

        assertNotNull(responseCart);
        assertEquals(modifyCartRequest.getQuantity(), responseCart.getItems().size());
        assertTrue(responseCart.getItems().contains(foundItem));
        assertEquals(requestItemPrice.multiply(BigDecimal.valueOf(modifyCartRequest.getQuantity())),
                responseCart.getTotal());

        verify(userRepositoryMock).findByUsername(modifyCartRequest.getUsername());
        verify(itemRepositoryMock).findById(modifyCartRequest.getItemId());
        verify(cartRepositoryMock).save(any());
    }

    @Test
    public void testAddToCartUnhappyPathUserNotFound() {
        // Arrange
        val modifyCartRequest = getModifyCartRequest();

        val userRepositoryMock = mock(UserRepository.class);
        when(userRepositoryMock.findByUsername(modifyCartRequest.getUsername())).thenReturn(null);

        val cartController = new CartController(userRepositoryMock, null, null);

        // Act
        val cartResponseEntity = cartController.addToCart(modifyCartRequest);

        // Assert
        assertNull(cartResponseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, cartResponseEntity.getStatusCode());

        verify(userRepositoryMock).findByUsername(modifyCartRequest.getUsername());
    }

    @Test
    public void testAddToCartUnhappyPathItemNotFound() {
        // Arrange
        val modifyCartRequest = getModifyCartRequest();

        val userRepositoryMock = mock(UserRepository.class);
        val foundUser = new User();
        foundUser.setCart(new Cart());
        when(userRepositoryMock.findByUsername(modifyCartRequest.getUsername())).thenReturn(foundUser);

        val itemRepositoryMock = mock(ItemRepository.class);
        when(itemRepositoryMock.findById(modifyCartRequest.getItemId())).thenReturn(Optional.empty());

        val cartController = new CartController(userRepositoryMock, null, itemRepositoryMock);

        // Act
        val cartResponseEntity = cartController.addToCart(modifyCartRequest);

        // Assert
        assertNull(cartResponseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, cartResponseEntity.getStatusCode());

        verify(userRepositoryMock).findByUsername(modifyCartRequest.getUsername());
        verify(itemRepositoryMock).findById(modifyCartRequest.getItemId());
    }

    @Test
    public void testRemoveFromCartHappyPath() {
        // Arrange
        val modifyCartRequest = getModifyCartRequest();
        val requestItem = new Item();
        requestItem.setId(modifyCartRequest.getItemId());
        requestItem.setPrice(BigDecimal.TEN);

        val userRepositoryMock = mock(UserRepository.class);
        val cart = new Cart();
        IntStream.range(0, modifyCartRequest.getQuantity()).forEach(i -> cart.addItem(requestItem));
        val foundUser = new User();
        foundUser.setCart(cart);
        when(userRepositoryMock.findByUsername(modifyCartRequest.getUsername())).thenReturn(foundUser);

        val itemRepositoryMock = mock(ItemRepository.class);
        val foundItem = new Item();
        foundItem.setId(requestItem.getId());
        foundItem.setPrice(requestItem.getPrice());
        when(itemRepositoryMock.findById(requestItem.getId())).thenReturn(Optional.of(foundItem));

        val cartRepositoryMock = mock(CartRepository.class);

        val cartController = new CartController(userRepositoryMock, cartRepositoryMock, itemRepositoryMock);

        // Act
        val cartResponseEntity = cartController.removeFromCart(modifyCartRequest);

        // Assert
        val responseCart = cartResponseEntity.getBody();

        assertNotNull(responseCart);
        assertEquals(0, responseCart.getItems().size());

        verify(userRepositoryMock).findByUsername(modifyCartRequest.getUsername());
        verify(itemRepositoryMock).findById(requestItem.getId());
        verify(cartRepositoryMock).save(any());
    }

    @Test
    public void testRemoveFromCartUnhappyPathUserNotFound() {
        // Arrange
        val modifyCartRequest = getModifyCartRequest();

        val userRepositoryMock = mock(UserRepository.class);
        when(userRepositoryMock.findByUsername(modifyCartRequest.getUsername())).thenReturn(null);

        val cartController = new CartController(userRepositoryMock, null, null);

        // Act
        val cartResponseEntity = cartController.removeFromCart(modifyCartRequest);

        // Assert
        assertNull(cartResponseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, cartResponseEntity.getStatusCode());

        verify(userRepositoryMock).findByUsername(modifyCartRequest.getUsername());
    }

    @Test
    public void testRemoveFromCartUnhappyPathItemNotFound() {
        // Arrange
        val modifyCartRequest = getModifyCartRequest();

        val userRepositoryMock = mock(UserRepository.class);
        val foundUser = new User();
        foundUser.setCart(new Cart());
        when(userRepositoryMock.findByUsername(modifyCartRequest.getUsername())).thenReturn(foundUser);

        val itemRepositoryMock = mock(ItemRepository.class);
        when(itemRepositoryMock.findById(modifyCartRequest.getItemId())).thenReturn(Optional.empty());

        val cartController = new CartController(userRepositoryMock, null, itemRepositoryMock);

        // Act
        val cartResponseEntity = cartController.removeFromCart(modifyCartRequest);

        // Assert
        assertNull(cartResponseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, cartResponseEntity.getStatusCode());

        verify(userRepositoryMock).findByUsername(modifyCartRequest.getUsername());
        verify(itemRepositoryMock).findById(modifyCartRequest.getItemId());
    }

    public ModifyCartRequest getModifyCartRequest() {
        val modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("testUser");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(2);
        return modifyCartRequest;
    }
}
