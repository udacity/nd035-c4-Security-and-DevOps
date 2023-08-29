package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.configuration.IMockitoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void creatUserSuccess(){
        when(encoder.encode("testductt16")).thenReturn("password1");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("ductt18");
        createUserRequest.setPassword("testductt16");
        createUserRequest.setConfirmPassword("testductt16");
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());
        User u = response.getBody();
        assertEquals("ductt18", u.getUsername());
        assertEquals("password1", u.getPassword());
    }

    @Test
    public void creatUserFailed(){
        when(encoder.encode("testductt16")).thenReturn("password1");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("ductt18");
        createUserRequest.setPassword("testdu");
        createUserRequest.setConfirmPassword("testdu");
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(400,response.getStatusCodeValue());
    }

    @Test
    public void findByIdTest(){
        User user = createUser();

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        ResponseEntity<User> response = userController.findById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void findByUsernameTest(){
        User user = createUser();

        when(userRepo.findByUsername("Username")).thenReturn(user);
        ResponseEntity<User> response = userController.findByUserName("Username");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        assertEquals(1L, user.getId());
        assertEquals("Username", user.getUsername());
        assertEquals("Password", user.getPassword());
    }


    public static User createUser(){
        User user = new User();

        user.setId(1);
        user.setUsername("Username");
        user.setPassword("Password");

        return user;
    }

}
