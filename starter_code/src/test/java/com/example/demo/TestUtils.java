package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.requests.CreateUserRequest;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestUtils {
    public static void injectObjects(Object target, String fieldName, Object toInject) {
        boolean wasPrivate = false;

        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            if (!f.isAccessible()) {
                f.setAccessible(true);
                wasPrivate = true;
            }

            f.set(target, toInject);
            if (wasPrivate) {
                f.setAccessible(false);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static User createUser() {
        User user = new User();
        Cart cart = createCart();
        cart.setUser(user);
        user.setId(100L);
        user.setUsername("udacity");
        user.setPassword("Password!");
        user.setCart(cart);
        return user;
    }

    public static Cart createCart() {
        Cart cart = new Cart();
        cart.setId(100L);
        cart.setItems(createItems());
        cart.setTotal(BigDecimal.valueOf(100L));
        return cart;
    }

    public static List<Item> createItems() {
        Item item1 = new Item();
        Item item2 = new Item();
        item1.setId(100L);
        item1.setName("Widget");
        item1.setPrice(BigDecimal.valueOf(50L));
        item1.setDescription("A Square Widget");
        item2.setId(101L);
        item2.setName("Gadget");
        item2.setPrice(BigDecimal.valueOf(50L));
        item2.setDescription("A Small Gadget");
        return new ArrayList<Item>() {{
            add(item1);
            add(item2);
        }};
    }

    public static CreateUserRequest createUserRequest() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test");
        userRequest.setPassword("testpassword");
        userRequest.setConfirmPassword("testpassword");
        return userRequest;
    }

    public static List<UserOrder> createUserOrders() {
        UserOrder userOrder = new UserOrder();
        List<Item> items = createItems();
        BigDecimal total = items.stream().map(Item::getPrice).reduce(BigDecimal::add).get();
        userOrder.setItems(items);
        userOrder.setTotal(total);
        return Arrays.asList(userOrder);
    }
}
