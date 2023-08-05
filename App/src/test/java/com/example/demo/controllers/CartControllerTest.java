package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartControllerTest {

    @Autowired
    private CartController cartController;
    @Autowired
    private UserController userController;

    @Test
    @Transactional
    public void addToCartTest() {

        // Test for successful addToCart()
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("UserCartTest");
        request.setPassword("Password");
        request.setConfirmPassword("Password");

        User user = userController.createUser(request).getBody();


        ModifyCartRequest request2 = new ModifyCartRequest();
        request2.setUsername("UserCartTest");
        request2.setItemId(1);
        request2.setQuantity(1);

        Cart cart = cartController.addTocart(request2).getBody();

        Assert.assertEquals(1, cart != null ? cart.getItems().size() : 0);

        // Test for unsuccessful addToCart()


        ModifyCartRequest request3 = new ModifyCartRequest();
        request3.setUsername("Unknown");
        request3.setQuantity(2);
        request3.setItemId(1);
        // Check http-status 404
        Assert.assertEquals(404, cartController.addTocart(request3).getStatusCodeValue());

    }

    // Test for removeFromCart()

    @Test
    @Transactional
    public void removeFromCartTest(){

        // create user

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("UserCartTest2");
        request.setPassword("Password");
        request.setConfirmPassword("Password");

        User user = userController.createUser(request).getBody();

        // addToCart

        ModifyCartRequest request2 = new ModifyCartRequest();
        request2.setUsername("UserCartTest2");
        request2.setItemId(1);
        request2.setQuantity(2);
        
        // Check addToCart is working correkt

        Cart cart = cartController.addTocart(request2).getBody();

        Assert.assertEquals(2, cart != null ? cart.getItems().size() : 0);
        
        // removeFromCart

        ModifyCartRequest request3 = new ModifyCartRequest();
        request3.setUsername("UserCartTest2");
        request3.setItemId(1);
        request3.setQuantity(1);

        Cart cart2 = cartController.removeFromcart(request3).getBody();

        Assert.assertEquals(1, user != null ? user.getCart().getItems().size() : 0);

    }


}
