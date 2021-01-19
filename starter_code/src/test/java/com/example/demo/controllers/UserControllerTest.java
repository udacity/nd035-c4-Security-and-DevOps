package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private static UserController userController;
    private static ResponseEntity<User> userResponseEntity;
    private static final UserRepository userRepository = mock(UserRepository.class);
    private static final CartRepository cartRepository = mock(CartRepository.class);
    private static final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @BeforeClass
    public static void setup() {
        userController = new UserController();
        CreateUserRequest userRequest = TestUtils.createUserRequest();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
        userResponseEntity = userController.createUser(userRequest);
    }

    @Test
    public void verifyCreateUser() {

        when(encoder.encode("testpassword2")).thenReturn("hashedpassword");

        CreateUserRequest newUserRequest = new CreateUserRequest();
        newUserRequest.setUsername("test2");
        newUserRequest.setPassword("testpassword2");
        newUserRequest.setConfirmPassword("testpassword2");

        ResponseEntity<User> responseEntity = userController.createUser(newUserRequest);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        User user = responseEntity.getBody();
        assertNotNull(user);

        assertEquals("test2", user.getUsername());
        assertEquals("hashedpassword", user.getPassword());
    }

    @Test
    public void verify400ErrorWhenPasswordTooShort() {
        CreateUserRequest newUserRequest = new CreateUserRequest();
        newUserRequest.setUsername("test3");
        newUserRequest.setPassword("pass");
        newUserRequest.setConfirmPassword("pass");
        ResponseEntity<User> responseEntity = userController.createUser(newUserRequest);
        assertEquals(400, responseEntity.getStatusCodeValue());
    }

    @Test
    public void verify400ErrorWhenPasswordDoesNotMatch() {
        CreateUserRequest newUserRequest = new CreateUserRequest();
        newUserRequest.setUsername("test4");
        newUserRequest.setPassword("pass");
        newUserRequest.setConfirmPassword("ssap");
        ResponseEntity<User> responseEntity = userController.createUser(newUserRequest);
        assertEquals(400, responseEntity.getStatusCodeValue());
    }

    @Test
    public void verifyFindByUserName() {
        when(userRepository.findByUsername("test")).thenReturn(userResponseEntity.getBody());
        ResponseEntity<User> response = userController.findByUserName("test");
        User user = response.getBody();
        assertNotNull(user);
        assertEquals("test", user.getUsername());
        assertEquals(0, user.getId());

    }

    @Test
    public void verify404ErrorWhenUserNameDoesNotExist() {
        when(userRepository.findByUsername("doesnotexist")).thenReturn(null);
        ResponseEntity<User> response = userController.findByUserName("doesnotexist");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void verifyFindById() {
        Optional<User> userOptional = Optional.of(userResponseEntity.getBody());
        when(userRepository.findById(0L)).thenReturn(userOptional);
        ResponseEntity<User> response = userController.findById(0L);
        User user = response.getBody();
        assertNotNull(user);
        assertEquals("test", user.getUsername());
        assertEquals(0, user.getId());
    }
}
