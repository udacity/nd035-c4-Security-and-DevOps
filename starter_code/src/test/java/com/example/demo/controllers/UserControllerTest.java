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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);



    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception {
        // TODO: create stabbing with encoder
        when(encoder.encode("testpassword")).thenReturn("thisIsHashed");
        // TODO: create user request
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testpassword");
        createUserRequest.setConfirmPassword("testpassword");

        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void find_user_by_id_happy_path() throws Exception{
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setPassword("testpassword");
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User userResponse = response.getBody();
        assertNotNull(userResponse);
        assertEquals(1, userResponse.getId());
        assertEquals("test", userResponse.getUsername());
        assertEquals("testpassword", userResponse.getPassword());
    }
}
