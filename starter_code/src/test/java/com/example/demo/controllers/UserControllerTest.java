package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static com.example.demo.ObjectsTest.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @DisplayName("Test create user ok")
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

    @DisplayName("Test create user error length password")
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

    @DisplayName("Test find user by id")
    @Test
    public void find_by_id_happy_path(){
        final Long id = 1L;
        final User user = userMock();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        final ResponseEntity<User> response =
                userController.findById(id);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        User userResponse = response.getBody();
        assertNotNull(userResponse);
        assertEquals(1, userResponse.getId());
        assertEquals("user1", userResponse.getUsername());
    }

    @DisplayName("Test not found user by id")
    @Test
    public void find_by_id_not_found(){
        final Long id = 1L;
        final ResponseEntity<User> response =
                userController.findById(id);
        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
    }

    @DisplayName("Test find user by username")
    @Test
    public void find_by_username_happy_path(){
        final String username = "user1";
        final User user = userMock();
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        final ResponseEntity<User> response =
                userController.findByUserName(username);
        assertNotNull(response);
        User userResponse = response.getBody();
        assertNotNull(userResponse);
        assertEquals(1, userResponse.getId());
        assertEquals("user1", userResponse.getUsername());
    }


}