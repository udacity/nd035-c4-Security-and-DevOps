package uz.jumanazar.ecommerceapp;

import uz.jumanazar.ecommerceapp.model.persistence.Cart;
import uz.jumanazar.ecommerceapp.model.persistence.Item;
import uz.jumanazar.ecommerceapp.model.persistence.User;
import uz.jumanazar.ecommerceapp.model.persistence.UserOrder;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static void injectObjects(Object target, String fieldName, Object toInject){
        boolean wasPrivate = false;

        try {
            Field declaredField = target.getClass().getDeclaredField(fieldName);

            if(!declaredField.isAccessible()){
                declaredField.setAccessible(true);
                wasPrivate = true;
            }
            declaredField.set(target, toInject);

            if(wasPrivate){
                declaredField.setAccessible(false);
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    public static User getUser(){
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("test1234");
        user.setCart(getCart());
        return user;
    }

    public static Cart getCart(){
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(getItems());
        return cart;
    }

    public static List<Item> addToItems(Item item){
        List<Item> items = getItems();
        items.add(item);
        return items;
    }

    public static List<Item> getItems(){
        List<Item> items = new ArrayList<>();
        Item item1 = getItem(1L, "Bananas", 1000d, "Food");

        items.add(item1);
        return items;
    }

    public static Item getItem(Long id, String name, Double price, String description){
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setPrice(new BigDecimal(price));
        item.setDescription(description);
        return item;
    }

    public static UserOrder getOrder() {
        UserOrder order = new UserOrder();
        order.setId(1L);
        order.setItems(getItems());
        BigDecimal totalPrice = getItems().stream().map(Item::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(totalPrice);
        order.setUser(getUser());
        return order;
    }
}
