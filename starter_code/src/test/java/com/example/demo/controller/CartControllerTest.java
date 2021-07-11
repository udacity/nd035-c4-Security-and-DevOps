package com.example.demo.controller;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

import com.example.demo.model.requests.ModifyCartRequest;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CartControllerTest {

  @InjectMocks
    private CartController cartController;


    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup()
    {
        cartController=new CartController();
        TestUtils.injectObjects(cartController,"userRepository",userRepository);
        TestUtils.injectObjects(cartController,"cartRepository",cartRepository);
        TestUtils.injectObjects(cartController,"itemRepository",itemRepository);

        User u= new User();
        u.setUsername("test");
        u.setId(2);
        u.setCart(new Cart());
        when(userRepository.findByUsername(anyString())).thenReturn(u);

        Item items= new Item();
        items.setId(1L);
        items.setDescription("abc");
        items.setName("item1");
        items.setPrice(BigDecimal.valueOf(20.0));
        when(itemRepository.findById(anyLong())).thenReturn(java.util.Optional.of(items));
    }

    @Test
    public void verify_item_addition()
    {
        ModifyCartRequest mr=new ModifyCartRequest();
        mr.setItemId(1L);
        mr.setQuantity(2);
        mr.setUsername("test");
        ResponseEntity<Cart> response= cartController.addTocart(mr);
        assertEquals(200,response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart()
    {
        ModifyCartRequest mr=new ModifyCartRequest();
        mr.setItemId(1L);
        mr.setQuantity(2);
        mr.setUsername("test");
        ResponseEntity<Cart> response= cartController.removeFromcart(mr);
        assertEquals(200,response.getStatusCodeValue());

    }

}
