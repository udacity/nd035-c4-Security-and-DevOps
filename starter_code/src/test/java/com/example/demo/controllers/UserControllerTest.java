package com.example.demo.controllers;

import com.example.demo.utils.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    UserController userController;
    private UserRepository userRepository=mock(UserRepository.class);
    private CartRepository cartRepository=mock(CartRepository.class);
    private BCryptPasswordEncoder encoder=mock(BCryptPasswordEncoder.class);
    @Before
    public void setup(){
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception{
        when(encoder.encode("testPassword")).thenReturn("this is Hashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0,user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("this is Hashed", user.getPassword());

    }

    @Test
    public void create_user_bad_request() throws Exception{
        when(encoder.encode("testPassword")).thenReturn("this is Hashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("test");
        createUserRequest.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());

    }

    @Test
    public void findbyusername_happy_path() throws Exception{
        when(encoder.encode("testPassword")).thenReturn("this is Hashed");
        ResponseEntity<User> response = createNewUser();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = new User();
        user.setUsername("test");
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        response = userController.findByUserName(response.getBody().getUsername());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        user = response.getBody();
        assertNotNull(user);
        assertEquals("test", user.getUsername());

    }
    @Test
    public void findbyid_happy_path() throws Exception{
        when(encoder.encode("testPassword")).thenReturn("this is Hashed");
        ResponseEntity<User> response = createNewUser();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = new User();
        user.setUsername("test");
        Optional<User> optionalUser = Optional.of(user);
        when(userRepository.findById(anyLong())).thenReturn((optionalUser));
        response = userController.findById(0L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        user = response.getBody();
        assertNotNull(user);
        assertEquals("test", user.getUsername());

    }

    @Test
    public void findbyid_not_found() throws Exception{
        when(encoder.encode("testPassword")).thenReturn("this is Hashed");
        ResponseEntity<User> response = createNewUser();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        response = userController.findById(0L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        User user = response.getBody();
        assertNull(user);


    }
    private ResponseEntity<User> createNewUser(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        return userController.createUser(createUserRequest);
    }


}