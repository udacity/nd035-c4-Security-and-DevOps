package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Item createItem(long id) {
        Item item = new Item();
        item.setId(id);
        item.setName("item " + id);
        item.setPrice(BigDecimal.valueOf(id));
        item.setDescription("description " + id);
        return item;
    }

    public static List<Item> createItems() {
        List<Item> items = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            items.add(createItem(i));
        }
        return items;
    }

    public static User createUser(long id) {
        User user = new User();
        user.setId(id);
        user.setUsername("test");
        user.setPassword("testPassword");
        return user;
    }

    public static Cart createCart(long id) {
        List<Item> items = createItems();
        User user = createUser(1);
        Cart cart = new Cart();
        items.forEach(cart::addItem);
        cart.setId(id);
        cart.setUser(user);
        return cart;
    }

    public static List<UserOrder> createOrders() {
        List<UserOrder> orders = new ArrayList<>();

        IntStream.range(0, 2).forEach(i -> {
            UserOrder order = new UserOrder();
            User user = createUser(1);
            Cart cart = createCart(1);
            cart.setUser(user);

            order.setItems(cart.getItems());
            order.setTotal(cart.getTotal());
            order.setUser(createUser(1));
            order.setId(Long.valueOf(i));

            orders.add(order);
        });
        return orders;
    }
}
