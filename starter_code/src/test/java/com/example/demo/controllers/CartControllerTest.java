package com.example.demo.controllers;

import com.example.demo.utils.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    UserController userController;
    ItemController itemController;
    OrderController orderController;
    CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup() {
        orderController = new OrderController();
        userController = new UserController();
        itemController = new ItemController();
        cartController = new CartController();
        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", encoder);
        TestUtils.injectObject(orderController, "userRepository", userRepository);
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);

        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);
    }

    private ResponseEntity<User> createNewUser() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        return userController.createUser(createUserRequest);
    }

    @Test
    public void addTocartTest() {
        User user = new User();
        user.setUsername("test");



        Item items = new Item();
        items.setDescription("bicycle");
        items.setId(0L);
        items.setName("bicycle");
        items.setPrice(new BigDecimal(880.00));


        Cart cart = new Cart();
        cart.addItem(items);
        user.setCart(cart);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setQuantity(1);

        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(items));
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void addTocartTest_cannot_find_name() {
        User user = new User();
        user.setUsername("test");


        Item items = new Item();
        items.setDescription("bicycle");
        items.setId(0L);
        items.setName("bicycle");
        items.setPrice(new BigDecimal(880.00));


        Cart cart = new Cart();
        cart.addItem(items);
        //cart.getTotal();
        //cart.getItems();
        //cart.

        user.setCart(cart);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setQuantity(1);

        //when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(items));
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void addTocartTest_cannot_find_item() {
        User user = new User();
        user.setUsername("test");


        Item items = new Item();
        items.setDescription("bicycle");
        items.setId(0L);
        items.setName("bicycle");
        items.setPrice(new BigDecimal(880.00));


        Cart cart = new Cart();
        cart.addItem(items);
        user.setCart(cart);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setQuantity(1);

        when(userRepository.findByUsername(anyString())).thenReturn(user);
        //when(itemRepository.findById(anyLong())).thenReturn(Optional.of(items));
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void removeFromCartTest() {
        User user = new User();
        user.setUsername("test");


        Item items = new Item();
        items.setDescription("bicycle");
        items.setId(0L);
        items.setName("bicycle");
        items.setPrice(new BigDecimal(880.00));


        Cart cart = new Cart();
        cart.addItem(items);
        user.setCart(cart);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setQuantity(1);

        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(items));
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());


    }

    @Test
    public void removeFromCartTest_cannot_find_name() {
        User user = new User();
        user.setUsername("test");


        Item items = new Item();
        items.setDescription("bicycle");
        items.setId(0L);
        items.setName("bicycle");
        items.setPrice(new BigDecimal(880.00));


        Cart cart = new Cart();
        cart.addItem(items);
        user.setCart(cart);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setQuantity(1);

        //when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(items));
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void removeFromCartTest_cannot_find_item() {
        User user = new User();
        user.setUsername("test");


        Item items = new Item();
        items.setDescription("bicycle");
        items.setId(0L);
        items.setName("bicycle");
        items.setPrice(new BigDecimal(880.00));


        Cart cart = new Cart();
        cart.addItem(items);
        user.setCart(cart);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setQuantity(1);

        when(userRepository.findByUsername(anyString())).thenReturn(user);
        //when(itemRepository.findById(anyLong())).thenReturn(Optional.of(items));
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }
}