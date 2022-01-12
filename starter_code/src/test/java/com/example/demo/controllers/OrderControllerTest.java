package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.gen5.api.Assertions.assertEquals;
import static org.junit.gen5.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    public static final String USERNAME = "test";
    private OrderController orderController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        orderController = new OrderController();
        com.example.demo.TestUtils.injectObjects(orderController, "userRepository", userRepository);
        com.example.demo.TestUtils.injectObjects(orderController, "orderRepository", orderRepository);

    }


    @Test
    public void submit_order() {


        User user = new User();
        user.setUsername(USERNAME);
        Item item = GetItemsUtils.getItem0();
        Cart cart = new Cart();
        cart.setId(0L);
        cart.setUser(user);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);
        cart.setTotal(BigDecimal.valueOf(2.99));
        user.setCart(cart);
        when(userRepository.findByUsername(USERNAME)).thenReturn(user);
        ResponseEntity<UserOrder> response = orderController.submit(USERNAME);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        UserOrder userResponseBody = response.getBody();
        assertNotNull(userResponseBody);
        List<Item> items = userResponseBody.getItems();
        assertNotNull(items);
        Item retrievedItem = items.get(0);
        assertEquals(1, items.size());
        assertEquals(new BigDecimal("2.99"), userResponseBody.getTotal());
        assertEquals(user, userResponseBody.getUser());

    }

    @Test
    public void testSubmitNullUser() {
        String username = "test";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setId(0L);
        Item item = com.example.demo.controllers.GetItemsUtils.getItem0();
        Cart cart = new Cart();
        cart.setId(0L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);
        cart.setTotal(new BigDecimal("2.99"));
        cart.setUser(user);
        user.setCart(cart);
        when(userRepository.findByUsername(username)).thenReturn(null);

        ResponseEntity<UserOrder> response =  orderController.submit(username);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void user_order_history(){

        Item item = createItem(1L, "Fidget Spinner", BigDecimal.valueOf(5), "Metal Toy");
        ArrayList<Item> items = new ArrayList<>();
        items.add(item);
        Cart cart = createCart(1L, new ArrayList<>(), null);
        User user = createUser(1L, "Rizwan", "12345678", null);
        cart.setUser(user);
        cart.setItems(items);
        user.setCart(cart);

        orderController.submit("Rizwan");
        when(userRepository.findByUsername("Rizwan")).thenReturn(user);

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("Rizwan");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> orders = response.getBody();
        assertNotNull(orders);

    }

    @Test
    public void testGetOrdersForUserNullUser() {
        String username = "test";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setId(0L);
        Item item = com.example.demo.controllers.GetItemsUtils.getItem0();
        Cart cart = new Cart();
        cart.setId(0L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);
        cart.setTotal(new BigDecimal("2.99"));
        cart.setUser(user);
        user.setCart(cart);
        when(userRepository.findByUsername(username)).thenReturn(null);

        orderController.submit(username);

        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser(username);

        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    /**
     * Create user
     *
     * @param userId
     * @param username
     * @param password
     * @param cart
     * @return newUser
     */
    public User createUser(long userId, String username, String password, Cart cart) {
        User newUser = new User();
        newUser.setId(userId);
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setCart(cart);
        return newUser;
    }



    /**
     * Create item
     *
     * @param id
     * @param name
     * @param price
     * @param description
     * @return newItem
     */
    public Item createItem(Long id, String name, BigDecimal price, String description) {
        Item newItem = new Item();
        newItem.setId(id);
        newItem.setName(name);
        newItem.setPrice(price);
        newItem.setDescription(description);
        return newItem;
    }

    /**
     * Create Cart
     *
     * @param cartId
     * @param items
     * @param user
     * @return newCart
     */
    public Cart createCart(long cartId, ArrayList<Item> items, User user) {
        Cart newCart = new Cart();
        newCart.setId(cartId);
        newCart.setItems(items);
        newCart.setUser(user);
        newCart.setTotal(BigDecimal.valueOf(5));
        return newCart;
    }
}
