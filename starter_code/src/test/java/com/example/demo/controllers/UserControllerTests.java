package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.exception.ResourceException;
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

public class UserControllerTests {
    private UserController userController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    private User userTest;

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);

        userTest = new User();
        userTest.setId(1);
        userTest.setUsername("UserTest_0");
        userTest.setPassword("PasswordTest_0");
    }

    @Test
    public void whenCreateUserShouldReturnANewUserWhenSucceed() {
        when(bCryptPasswordEncoder.encode("PasswordTest")).thenReturn("HashedPassword");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("UserTest");
        userRequest.setPassword("PasswordTest");
        userRequest.setConfirmPassword("PasswordTest");

        ResponseEntity<User> userResponse = userController.createUser(userRequest);
        assertNotNull(userResponse);
        assertEquals(200, userResponse.getStatusCodeValue());

        User user = userResponse.getBody();
        assertEquals(0, user.getId());
        assertEquals("UserTest", user.getUsername());
        assertEquals("HashedPassword", user.getPassword());
    }

    @Test
    public void whenFindUserByIdShouldReturnAUser() {
        long userId = 0L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(userTest));
        final ResponseEntity<User> userResponse = userController.findById(userId);
        assertNotNull(userResponse);
        assertEquals(200, userResponse.getStatusCodeValue());
        User user = userResponse.getBody();

        assertEquals(userTest.getId(), user.getId());
        assertEquals(userTest.getUsername(), user.getUsername());
        assertEquals(userTest.getPassword(), user.getPassword());
    }

    @Test
    public void whenFindUserByNameShouldReturnAUser() {
        String userName = "UserTest_0";

        when(userRepository.findByUsername(userName)).thenReturn(userTest);
        ResponseEntity<User> userResponse = userController.findByUserName(userName);
        assertNotNull(userResponse);
        assertEquals(200, userResponse.getStatusCodeValue());
        User user = userResponse.getBody();

        assertEquals(userTest.getId(), user.getId());
        assertEquals(userTest.getUsername(), user.getUsername());
        assertEquals(userTest.getPassword(), user.getPassword());
    }

    @Test(expected = ResourceException.class)
    public void whenCreateUserWithInvalidPasswordShouldThrowResourceException() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("UserToFail");
        userRequest.setPassword("");
        userRequest.setConfirmPassword("");

        userController.createUser(userRequest);
    }

    @Test(expected = ResourceException.class)
    public void whenCreateUserWithInvalidConfirmationPasswordShouldThrowResourceException() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("UserToFail");
        userRequest.setPassword("PasswordTest");
        userRequest.setConfirmPassword("PasswordTestWrong");

        userController.createUser(userRequest);
    }
}
