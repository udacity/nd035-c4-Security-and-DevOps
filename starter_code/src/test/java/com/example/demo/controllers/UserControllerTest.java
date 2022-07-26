package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void create_user_happy_path(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("user1");
        createUserRequest.setPassword("1234567");
        createUserRequest.setConfirmPassword("1234567");
        final ResponseEntity<User> response =
                userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("user1", user.getUsername());
    }

    @Test
    public void create_user_password_not_length(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("user1");
        createUserRequest.setPassword("123");
        createUserRequest.setConfirmPassword("123");
        final ResponseEntity<User> response =
                userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
    }
}