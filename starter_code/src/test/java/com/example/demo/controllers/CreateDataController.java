package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CreateDataController {
    public static User createUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("username");
        user.setPassword("Password@12");
        user.setCart(new Cart());
        return user;
    }

    public static Cart createCart() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setTotal(null);
        cart.setItems(new ArrayList<>());
        cart.setTotal(BigDecimal.valueOf(0.0));
        return cart;
    }

    public static Item createItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("Item");
        item.setPrice(BigDecimal.valueOf(100.0));
        return item;
    }
}
