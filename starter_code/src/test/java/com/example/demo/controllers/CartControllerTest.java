package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.example.demo.ObjectsTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setUp(){
        final User user = userMock();
        when(userRepository.findByUsername(anyString()))
                .thenReturn(user);
        Item item = itemMock();
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
    }

    @DisplayName("Test add to Cart OK")
    @Test
    public void add_to_cart_ok(){
        ModifyCartRequest modifyCartRequest = modifyCartRequestMock();
        final ResponseEntity<Cart> response =
                cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        final Cart cart = response.getBody();
        assertNotNull(cart);
        assertEquals(new BigDecimal(200), cart.getTotal());
    }

    @DisplayName("Test remove items from cart")
    @Test
    public void remove_from_cart(){
        ModifyCartRequest modifyCartRequest = modifyCartRequestMock();
        final ResponseEntity<Cart> response =
                cartController.removeFromcart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        final Cart cart = response.getBody();
        assertNotNull(cart);
    }
}