package com.example.demo.controllers;

import com.example.demo.Helper;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
   }

   @Test
   public void add_to_cart_happy_test(){

        User user = Helper.createUser();
        Item item = Helper.createItem();
        Cart cart = user.getCart();
        cart.setUser(user);
        cart.addItem(item);
        user.setCart(cart);

        when(userRepository.findByUsername("netcha")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

       ModifyCartRequest request = Helper.createModifiedCartrequest();

       ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(request);
       Assert.assertNotNull(cartResponseEntity);

       Assert.assertEquals(HttpStatus.OK, cartResponseEntity.getStatusCode());
       Assert.assertNotNull(cartResponseEntity.getBody());
       Assert.assertNotNull(cartResponseEntity.getBody().getItems());
    }

    @Test
    public void add_to_cart_user_not_found_test(){

        ModifyCartRequest request = Helper.createModifiedCartrequest();

        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(request);
        Assert.assertNotNull(cartResponseEntity);

        Assert.assertEquals(HttpStatus.NOT_FOUND, cartResponseEntity.getStatusCode());
    }

    @Test
    public void add_to_cart_item_not_found_test(){

        ModifyCartRequest request = Helper.createModifiedCartrequest();

        User user = Helper.createUser();

        when(userRepository.findByUsername(any())).thenReturn(user);

        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(request);
        Assert.assertNotNull(cartResponseEntity);

        Assert.assertEquals(HttpStatus.NOT_FOUND, cartResponseEntity.getStatusCode());
    }

    @Test
    public void remove_to_cart_happy_test(){

        User user = Helper.createUser();
        Item item = Helper.createItem();
        Cart cart = user.getCart();
        cart.setUser(user);
        cart.addItem(item);

        when(userRepository.findByUsername("netcha")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ModifyCartRequest request = Helper.createModifiedCartrequest();

        ResponseEntity<Cart> cartResponseEntity = cartController.removeFromcart(request);
        Assert.assertNotNull(cartResponseEntity);

        Assert.assertEquals(HttpStatus.OK, cartResponseEntity.getStatusCode());
        Assert.assertNotNull(cartResponseEntity.getBody());
        List<Item> itemList = cartResponseEntity.getBody().getItems();
        Assert.assertEquals(0, itemList.size());
    }

    @Test
    public void remove_from_cart_user_not_found_test(){

        ModifyCartRequest request = Helper.createModifiedCartrequest();

        ResponseEntity<Cart> cartResponseEntity = cartController.removeFromcart(request);
        Assert.assertNotNull(cartResponseEntity);

        Assert.assertEquals(HttpStatus.NOT_FOUND, cartResponseEntity.getStatusCode());
    }

    @Test
    public void remove_from_cart_item_not_found_test(){

        ModifyCartRequest request = Helper.createModifiedCartrequest();

        User user = Helper.createUser();

        when(userRepository.findByUsername(any())).thenReturn(user);

        ResponseEntity<Cart> cartResponseEntity = cartController.removeFromcart(request);
        Assert.assertNotNull(cartResponseEntity);

        Assert.assertEquals(HttpStatus.NOT_FOUND, cartResponseEntity.getStatusCode());
    }

}
