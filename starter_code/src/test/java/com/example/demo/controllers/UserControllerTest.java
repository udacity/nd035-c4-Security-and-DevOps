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
    private UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);

    }

    @Test
    public void create_user_happy_path() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("thisIsHashed", u.getPassword());
        assertEquals("test", u.getUsername());

    }

    @Test
    public void verify_findById() throws Exception {
        User user = new User();
        user.setUsername("Arvin");
        user.setId(1L);
        user.setPassword("somePassword");

        // Test case when the user exists
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.findById(user.getId());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User userActual = response.getBody();
        assertNotNull(userActual);
        assertEquals(user, userActual);
        assertEquals("Arvin", userActual.getUsername());

        // Test case when the user does exists
        when(userRepo.findById(user.getId())).thenReturn(Optional.empty());

        ResponseEntity<User> anotherResponse = userController.findById(user.getId());
        assertNotNull(anotherResponse);
        assertEquals(404, anotherResponse.getStatusCodeValue());

    }

    @Test
    public void verify_findByUserName() throws Exception {
        User user = new User();
        user.setUsername("Messi");
        user.setId(1L);
        user.setPassword("messiPassword");

        // Test case when the user exists
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<User> response = userController.findByUserName(user.getUsername());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User userActual = response.getBody();
        assertNotNull(userActual);
        assertEquals(user, userActual);
        assertEquals("Messi", userActual.getUsername());

        // Test case when the user does exists
        when(userRepo.findByUsername(user.getUsername())).thenReturn(null);

        ResponseEntity<User> anotherResponse = userController.findById(user.getId());
        assertNotNull(anotherResponse);
        assertEquals(404, anotherResponse.getStatusCodeValue());

    }

}
