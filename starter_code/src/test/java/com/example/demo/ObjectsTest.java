package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.requests.ModifyCartRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ObjectsTest {

    public static User userMock(){
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setPassword("123456789");
        user.setCart(cartMock());
        return  user;
    }

    public static Cart cartMock(){
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setTotal(new BigDecimal(100));
        cart.setItems(itemListMock());
        return cart;
    }

    public static Item itemMock(){
        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setPrice(new BigDecimal(100));
        return item;
    }

    public static List<Item> itemListMock(){
        List<Item> itemList = new ArrayList<>();
        itemList.add(itemMock());
        return itemList;
    }

    public static UserOrder userOrderMock(){
        UserOrder userOrder = new UserOrder();
        userOrder.setId(1L);
        userOrder.setTotal(new BigDecimal(100));
        userOrder.setItems(Collections.singletonList(itemMock()));
        return userOrder;
    }

    public static List<UserOrder> userOrderListMock(){
        return Collections.singletonList(userOrderMock());
    }

    public static ModifyCartRequest modifyCartRequestMock(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername("user1");
        modifyCartRequest.setQuantity(1);
        return modifyCartRequest;
    }
}
