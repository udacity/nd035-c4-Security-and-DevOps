package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);

        User user = new User();
        Cart cart = new Cart();
        user.setId(0);
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setCart(cart);


        when(userRepository.findById(0L)).thenReturn(java.util.Optional.of(user));
        when(userRepository.findByUsername("testUsername")).thenReturn(user);
    }

    @Test
    public void createUser_happy_path() {
        when(encoder.encode(anyString())).thenReturn("thisIsHashed");

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("testUsername");
        r.setPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("testUsername", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void findById_happy_path() {
        final ResponseEntity<User> response = userController.findById(0L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());;

    }

    @Test
    public void findById_crazy_path() {
        final ResponseEntity<User> response = userController.findById(99L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        User u = response.getBody();
        assertNull(u);
    }


    @Test
    public void findByUserName_happy_path() {
        final ResponseEntity<User> response = userController.findByUserName("testUsername");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals("testUsername", u.getUsername());;

    }


}