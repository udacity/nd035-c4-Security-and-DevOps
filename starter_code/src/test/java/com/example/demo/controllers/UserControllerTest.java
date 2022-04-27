package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    public void create_user_happy_path(){
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test");
        userRequest.setPassword("testPassword");
        userRequest.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(userRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void create_user_unhappy_path(){
        when(encoder.encode("testPa")).thenReturn("thisIsHashed");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test");
        userRequest.setPassword("testPa");
        userRequest.setConfirmPassword("testPa");

        final ResponseEntity<User> response = userController.createUser(userRequest);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void find_user_by_username(){
        User userInput = new User();
        userInput.setUsername("test");
        when(userRepo.findByUsername("test")).thenReturn(userInput);
        ResponseEntity<User> response = userController.findByUserName("test");

        User user = response.getBody();
        assertEquals("test", user.getUsername());
    }

    @Test
    public void find_user_by_id(){
        User userInput = new User();
        userInput.setUsername("test");
        Optional<User> optionalUserInput = Optional.of(userInput);
        Long id = Long.valueOf(0);
        when(userRepo.findById(id)).thenReturn(optionalUserInput);
        ResponseEntity<User> response = userController.findById(id);

        User user = response.getBody();
        assertEquals("test", user.getUsername());
    }
}
