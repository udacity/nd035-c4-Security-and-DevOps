package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Helper {

    public static CreateUserRequest createUserRequest(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("Netcha Ballack");
        createUserRequest.setPassword("netchas1234");
        createUserRequest.setConfirmPassword("netchas1234");
        return createUserRequest;
    }

    public static User createUser(){
        User user = new User();
        user.setId(1L);
        user.setUsername("netchas");
        user.setPassword("ballack");
        user.setCart(createcart());
        return user;
    }

    public static Cart createcart(){
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(null);
        cart.setTotal(BigDecimal.TEN);
        cart.setItems(new ArrayList<Item>());
        return cart;
    }

    public static Item createItem(){
        Item item = new Item();
        item.setId(1L);
        item.setDescription("description of item");
        item.setName("item name");
        item.setPrice(BigDecimal.TEN);
        return item;
    }

    public static ModifyCartRequest createModifiedCartrequest(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(10);
        request.setUsername("netcha");
        request.setItemId(1L);

        return request;
    }
}
