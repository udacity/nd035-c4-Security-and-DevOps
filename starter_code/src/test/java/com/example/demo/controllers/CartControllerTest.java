package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CartControllerTest {
    @Autowired
    private CartController cartController;
    @Autowired
    private UserController userController;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ItemRepository itemRepository;
    private Cart cart;
    private User user;

    private static Cart CreateMockCart(User user) {
        Cart mockCart = new Cart();
        mockCart.addItem(new Item(1L, "Eggs", new BigDecimal("1.23"), "Large Eggs"));
        mockCart.addItem(new Item(2L, "Ripe Bananas 5 Pack", new BigDecimal("0.79"), "Ripen At Home Bananas 5 Pack"));
        mockCart.setUser(user);
        return mockCart;

    }

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {

        itemRepository.saveAll(ItemControllerTest.CreateMockItems());

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");

        final ResponseEntity<User> responseEntity = userController.createUser(request);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        user = responseEntity.getBody();
        assert user != null;
        assertNotNull(user);
        assertEquals("test", user.getUsername());
        cart = CreateMockCart(user);
        cartRepository.save(cart);
    }

    @Test
    public void add_item_to_cart() {

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(2L);
        modifyCartRequest.setQuantity(5);
        Cart cart = cartController.addTocart(modifyCartRequest).getBody();
        assertEquals(java.util.Optional.ofNullable(cart.getId()), java.util.Optional.of(1L));
        assertEquals(5, cart.getItems().size());
        assertEquals(new BigDecimal("3.95"), cart.getTotal());
    }

    @Test
    public void remove_item_from_cart() {

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(2L);
        modifyCartRequest.setQuantity(5);
        Cart cart = cartController.addTocart(modifyCartRequest).getBody();
        assertEquals(5, cart.getItems().size());
        assertEquals(new BigDecimal("3.95"), cart.getTotal());
        modifyCartRequest.setItemId(2L);
        modifyCartRequest.setQuantity(1);
        cart = cartController.removeFromcart(modifyCartRequest).getBody();
        assertEquals(4, cart.getItems().size());

    }

}
