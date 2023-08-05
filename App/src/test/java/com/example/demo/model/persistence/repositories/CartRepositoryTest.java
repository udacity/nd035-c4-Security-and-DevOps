package com.example.demo.model.persistence.repositories;


import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartRepositoryTest {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    UserRepository userRepository;


    @Test
    @Transactional
    public void findByUserTest(){

        User user = new User();

        user.setUsername("John");
        user.setPassword("Password");

        Item item = new Item();
        item.setId(1L);
        item.setName("New Item");
        item.setPrice(BigDecimal.valueOf(5));
        item.setDescription("Test");

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(Collections.singletonList(item));
        user.setCart(cart);
        cartRepository.save(cart);
        userRepository.save(user);

        User user1 = userRepository.findByUsername("John");

        Cart foundCart = cartRepository.findByUser(user1);
        Assert.assertNotNull(foundCart);
        Assert.assertEquals("Test",cart.getItems().get(0).getDescription());
        Assert.assertEquals("New Item", cart.getItems().get(0).getName());

        // negative test

        User unknown = new User();
        unknown.setUsername("Marco");
        Cart unknownCart = cartRepository.findByUser(unknown);
        Assert.assertNull(unknownCart);

    }
}
