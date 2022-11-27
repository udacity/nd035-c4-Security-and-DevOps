package com.example.demo.controllers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

@RunWith(SpringRunner.class)
public class CartControllerUnit {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private CartController cartController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void given_validRequest_when_addToCart_then_return_successWithCart() {
        when(this.userRepository.findByUsername(any())).thenReturn(this.buildUser());
        when(this.itemRepository.findById(any())).thenReturn(this.buildItem());
        ModifyCartRequest request = this.buildModifyCartRequest();
        ResponseEntity<Cart> response = this.cartController.addTocart(request);
        assertNotNull(response);
    }

    @Test
    public void given_validRequest_but_userNotFound_when_addToCart_then_return_HTTPNOT_FOUND() {
        when(this.userRepository.findByUsername(any())).thenReturn(null);
        when(this.itemRepository.findById(any())).thenReturn(this.buildItem());
        ModifyCartRequest request = this.buildModifyCartRequest();
        ResponseEntity<Cart> response = this.cartController.addTocart(request);
        assertNotNull(response);
        assertTrue(response.getStatusCode()
            .is4xxClientError());
    }

    @Test
    public void given_validRequest_but_itemNotFound_when_addToCart_then_return_HTTPNOT_FOUND() {
        when(this.userRepository.findByUsername(any())).thenReturn(this.buildUser());
        when(this.itemRepository.findById(any())).thenReturn(this.buildInvalidItem());
        ModifyCartRequest request = this.buildModifyCartRequest();
        ResponseEntity<Cart> response = this.cartController.addTocart(request);
        assertNotNull(response);
        assertTrue(response.getStatusCode()
            .is4xxClientError());
    }

    /*
     * Tests: remove
     * Given valid request - but user not found, return HTTP NOT FOUND
     * Given valid request - but item not found, return HTTP NOT FOUND
     */

    @Test
    public void given_validRequest_when_removeFromCart_then_returnSuccessAndCart() {
        when(this.userRepository.findByUsername(any())).thenReturn(this.buildUser());
        when(this.itemRepository.findById(any())).thenReturn(this.buildItem());
        
        ModifyCartRequest request = this.buildModifyCartRequest();
        
        ResponseEntity<Cart> response = this.cartController.removeFromcart(request);
       
        assertNotNull(response);
    
    }
    
    @Test
    public void given_validRequest_but_userNotFound_when_removeFromCart_then_returnHTTPNOT_FOUND() {
        when(this.userRepository.findByUsername(any())).thenReturn(null);
        when(this.itemRepository.findById(any())).thenReturn(this.buildItem());
        
        ModifyCartRequest request = this.buildModifyCartRequest();
        
        ResponseEntity<Cart> response = this.cartController.removeFromcart(request);
       
        assertNotNull(response);
        assertTrue(response.getStatusCode().is4xxClientError());
    }
    
    @Test
    public void given_validRequest_but_itemNotFound_when_removeFromCart_then_returnHTTPNOT_FOUND() {
        when(this.userRepository.findByUsername(any())).thenReturn(this.buildUser());
        when(this.itemRepository.findById(any())).thenReturn(this.buildInvalidItem());
        
        ModifyCartRequest request = this.buildModifyCartRequest();
        
        ResponseEntity<Cart> response = this.cartController.removeFromcart(request);
       
        assertNotNull(response);
        assertTrue(response.getStatusCode().is4xxClientError());
    }

    private Optional<Item> buildItem() {
        Item item = new Item();
        item.setId((long) 1);
        item.setName("Hair Comb");
        item.setPrice(new BigDecimal(19.99));
        item.setDescription("Comb to brush hair");
        Optional<Item> oItem = Optional.ofNullable(item);
        return oItem;
    }

    private Optional<Item> buildInvalidItem() {
        Item item = null;
        Optional<Item> oItem = Optional.ofNullable(item);
        return oItem;
    }

    private User buildUser() {
        User user = new User();
        user.setId(1);
        user.setPassword("P@ssword!");
        user.setUsername("user1");
        Cart userCart = new Cart();
        userCart.setId((long) 1);
        userCart.setUser(user);
        user.setCart(userCart);
        return user;
    }

    private ModifyCartRequest buildModifyCartRequest() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(1);
        request.setUsername("user1");
        return request;
    }
}
