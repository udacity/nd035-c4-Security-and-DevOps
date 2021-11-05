package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    CreateUserRequest request;

    @Before
    public void setup(){
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", encoder);

        request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");
    }

    @Test
    public void createUserHappyPath() throws Exception{
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");

        Assert.assertEquals(request.getPassword(), request.getConfirmPassword());
        Assert.assertTrue(request.getPassword().length() >= 7);

        ResponseEntity<User> response = userController.createUser(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        Assert.assertNotNull(user);
        Assert.assertEquals(0, user.getId());
        Assert.assertEquals("test", user.getUsername());
        Assert.assertEquals("thisIsHashed", user.getPassword());

    }

    @Test
    public void testFindById(){
        ResponseEntity<User> response = userController.createUser(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        Assert.assertNotNull(user);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        ResponseEntity<User> NewResponse = userController.findById(user.getId());
        User savedUser = NewResponse.getBody();
        Assert.assertNotNull(savedUser);
        Assert.assertEquals(0, savedUser.getId());
        Assert.assertEquals(200, NewResponse.getStatusCodeValue());
    }

    @Test
    public void testFindByUserName(){
        ResponseEntity<User> response = userController.createUser(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        Assert.assertNotNull(user);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        ResponseEntity<User> NewResponse = userController.findByUserName(user.getUsername());
        User savedUser = NewResponse.getBody();
        Assert.assertNotNull(savedUser);
        Assert.assertEquals("test", savedUser.getUsername());
        Assert.assertEquals(200, NewResponse.getStatusCodeValue());

    }

}











