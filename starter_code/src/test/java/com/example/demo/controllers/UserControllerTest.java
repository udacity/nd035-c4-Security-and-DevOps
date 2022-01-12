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

import static org.junit.gen5.api.Assertions.assertEquals;
import static org.junit.gen5.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void create_a_user() throws Exception {
        CreateUserRequest newUserRequest = createUser();
        final ResponseEntity<User> userResponseEntity = userController.createUser(newUserRequest);
        assertNotNull(userResponseEntity);
        assertEquals(200, userResponseEntity.getStatusCodeValue());
        User user = userResponseEntity.getBody();
        assertNotNull(user);
        assertEquals(0L, user.getId());
        assertEquals("Rizwan", user.getUsername());
        assertEquals("this is hashed", user.getPassword());
    }


    @Test
    public void find_user_by_userName() throws Exception {
        CreateUserRequest newUserRequest = createUser();
        final ResponseEntity<User> userResponseEntity = userController.createUser(newUserRequest);
        assertNotNull(userResponseEntity);
        assertEquals(200, userResponseEntity.getStatusCodeValue());
        User u = userResponseEntity.getBody();
        when(userRepository.findByUsername(u.getUsername())).thenReturn(u);
        final ResponseEntity<User> responseEntity = userController.findByUserName(u.getUsername());
        User user = responseEntity.getBody();
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(user);
        assertEquals(0L, user.getId());
        assertEquals(user.getUsername(), "Rizwan");

    }

    @Test
    public void find_user_by_userId() throws Exception {
        CreateUserRequest newUserRequest = createUser();
        final ResponseEntity<User> userResponseEntity = userController.createUser(newUserRequest);
        assertEquals(200, userResponseEntity.getStatusCodeValue());
        User u = userResponseEntity.getBody();
        when(userRepository.findById(u.getId())).thenReturn(java.util.Optional.of(u));
        final ResponseEntity<User> responseEntity = userController.findById(u.getId());
        User user = responseEntity.getBody();
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(user);
        assertEquals(0L, user.getId());
        assertEquals(user.getUsername(), "Rizwan");
    }


    private CreateUserRequest createUser() {
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("this is hashed");
        CreateUserRequest newUserRequest = new CreateUserRequest();
        newUserRequest.setUsername("Rizwan");
        newUserRequest.setPassword("testPassword");
        newUserRequest.setConfirmPassword("testPassword");
        return newUserRequest;
    }
}

