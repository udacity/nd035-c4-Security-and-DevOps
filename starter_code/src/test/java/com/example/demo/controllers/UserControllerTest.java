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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    // call the methods of the controller class
    private UserController userController;

    // create a mock object of the repositories
    final private UserRepository userRepository = mock(UserRepository.class);
    final private CartRepository cartRepository = mock(CartRepository.class);
    final private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void verify_createUser() {
        when(encoder.encode("shipud")).thenReturn("naruto");
        CreateUserRequest newUser = new CreateUserRequest();
        newUser.setUsername("johnDoe");
        newUser.setPassword("shipud");
        newUser.setConfirmPassword("shipud");

        ResponseEntity<User> response = userController.createUser(newUser);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("johnDoe", user.getUsername());
        assertEquals("naruto", user.getPassword());
    }

    @Test
    public void verify_findById() {
        CreateUserRequest newUser = new CreateUserRequest();
        newUser.setUsername("johnDoe");
        newUser.setPassword("shipud");
        newUser.setConfirmPassword("shipud");

        ResponseEntity<User> createUserResponse = userController.createUser(newUser);
        final User body = createUserResponse.getBody();

        assertNotNull(createUserResponse);
        assertEquals(200, createUserResponse.getStatusCodeValue());

        assert body != null;
        when(userRepository.findById(body.getId())).thenReturn(Optional.of(body));
        ResponseEntity<User> response = userController.findById(body.getId());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void verify_findByUserName() {
        when(encoder.encode("shipud")).thenReturn("naruto");
        CreateUserRequest newUser = new CreateUserRequest();
        newUser.setUsername("johnDoe");
        newUser.setPassword("shipud");
        newUser.setConfirmPassword("shipud");

        ResponseEntity<User> createUserResponse = userController.createUser(newUser);
        final User body = createUserResponse.getBody();

        assertNotNull(createUserResponse);
        assertEquals(200, createUserResponse.getStatusCodeValue());

        assert body != null;
        when(userRepository.findByUsername(body.getUsername())).thenReturn(body);
        ResponseEntity<User> response = userController.findByUserName(body.getUsername());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

}