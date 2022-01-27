package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.ModifyCartRequest;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;

public class TestUtils {

    public static void injectObjects(Object target, String fieldName, Object toInject) {
        boolean wasPrivate = false;

        try {

            Field f = target.getClass().getDeclaredField(fieldName);

            if (!f.isAccessible()){
                f.setAccessible(true);
                wasPrivate = true;
            }

            f.set(target, toInject);

            if (wasPrivate){
                f.setAccessible(false);
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Cart emptyCart(){
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(null);
        cart.setItems(new ArrayList<Item>());
        cart.setTotal(BigDecimal.valueOf(0.0));
        return cart;
    }

    public static User createUser(){
        User user = new User();
        user.setId(1);
        user.setUsername("Username");
        user.setPassword("Password");
        user.setCart(emptyCart());

        return user;
    }

    public static Item createItem(){
        Item item = new Item();
        item.setId(1L);
        item.setName("Created Item");
        item.setDescription("This is created item");
        item.setPrice(BigDecimal.valueOf(10.0));
        return item;
    }

    public static ModifyCartRequest createModifyCartRequest(String username, long itemId, int quantity){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(username);
        modifyCartRequest.setItemId(itemId);
        modifyCartRequest.setQuantity(quantity);
        return modifyCartRequest;
    }
}

