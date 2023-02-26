package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserController userController;

    @Test
    public void testFindById_Success() {
        // Setup
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Execute
        ResponseEntity<User> response = userController.findById(userId);

        // Verify
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(user, response.getBody());
    }

    @Test
    public void testFindById_UserNotFound() {
        // Setup
        Long userId = 1L;
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Execute
        ResponseEntity<User> response = userController.findById(userId);

        // Verify
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testFindByUserName_Success() {
        // Setup
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(user);

        // Execute
        ResponseEntity<User> response = userController.findByUserName(username);

        // Verify
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(user, response.getBody());
    }

    @Test
    public void testFindByUserName_UserNotFound() {
        // Setup
        String username = "testuser";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(null);

        // Execute
        ResponseEntity<User> response = userController.findByUserName(username);

        // Verify
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateUser_Success() {
        // Setup
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testuser");
        createUserRequest.setPassword("password");
        createUserRequest.setConfirmPassword("password");

        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        Cart cart = new Cart();
        user.setCart(cart);

        String encodedPassword = "encodedPassword";
        Mockito.when(bCryptPasswordEncoder.encode(createUserRequest.getPassword())).thenReturn(encodedPassword);

        // Execute
        ResponseEntity<User> response = userController.createUser(createUserRequest);

        // Verify
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(user.getUsername(), response.getBody().getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void testCreateUser_PasswordTooShort() {
        // Setup
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testuser");
        createUserRequest.setPassword("pwd");
        createUserRequest.setConfirmPassword("pwd");

        // Execute
        ResponseEntity<User> response = userController.createUser(createUserRequest);

        // Verify
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCreateUser_PasswordsDoNotMatch() {
        // Setup
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testuser");
        createUserRequest.setPassword("password");
        createUserRequest.setConfirmPassword("notTheSamePassword");

        // Execute
        ResponseEntity<User> response = userController.createUser(createUserRequest);

        // Verify
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


}
