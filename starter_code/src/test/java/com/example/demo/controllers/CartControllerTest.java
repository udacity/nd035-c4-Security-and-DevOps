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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private ItemRepository itemRepo = mock(ItemRepository.class);


    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
    }

    @Test
    public void add_to_cart_happy_path(){
        ModifyCartRequest request= new ModifyCartRequest();
        request.setUsername("AlperHi");
        request.setItemId(Long.valueOf(0));
        request.setQuantity(3);

        Cart testCart = new Cart();

        User testUser = new User();
        testUser.setUsername("AlperHi");
        testUser.setCart(testCart);
        testCart.setUser(testUser);

        Item testItem = new Item();
        BigDecimal price = new BigDecimal(3);
        testItem.setPrice(price);
        testItem.setId(Long.valueOf(0));
        Optional optionalTestItem = Optional.of(testItem);

        when(userRepo.findByUsername(request.getUsername())).thenReturn(testUser);
        when(itemRepo.findById(request.getItemId())).thenReturn(optionalTestItem);

        final ResponseEntity<Cart> response = cartController.addTocart(request);

        Cart cart = response.getBody();

        BigDecimal total = new BigDecimal(9);
        assertEquals(total, cart.getTotal());
    }

    @Test
    public void add_to_cart_unhappy_path_no_user(){
        ModifyCartRequest request= new ModifyCartRequest();
        request.setUsername("AlperHi");
        request.setItemId(Long.valueOf(0));
        request.setQuantity(3);

        final ResponseEntity<Cart> response = cartController.addTocart(request);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void add_to_cart_unhappy_path_no_item(){
        ModifyCartRequest request= new ModifyCartRequest();
        request.setUsername("AlperHi");
        request.setItemId(Long.valueOf(0));
        request.setQuantity(3);

        Cart testCart = new Cart();

        User testUser = new User();
        testUser.setUsername("AlperHi");
        testUser.setCart(testCart);
        testCart.setUser(testUser);

        when(userRepo.findByUsername(request.getUsername())).thenReturn(testUser);

        final ResponseEntity<Cart> response = cartController.addTocart(request);

        assertEquals(404, response.getStatusCodeValue());
    }
}
