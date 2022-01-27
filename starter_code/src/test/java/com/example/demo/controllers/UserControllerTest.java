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

import static com.example.demo.TestUtils.createUser;
import static org.mockito.Mockito.mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private final UserRepository userRepo = mock(UserRepository.class);

    private final CartRepository cartRepo = mock(CartRepository.class);

    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUserHappyPath() throws Exception{
        when(encoder.encode("Password")).thenReturn("HashedPassword");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("Username");
        userRequest.setPassword("Password");
        userRequest.setConfirmpassword("Password");

        ResponseEntity<User> response = userController.createUser(userRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("Username", u.getUsername());
        assertEquals("HashedPassword", u.getPassword());
    }

    @Test
    public void findByIdFoundTest(){

        User user = createUser();
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        ResponseEntity<User> response = userController.findById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
    }

    @Test
    public void findByIdNotFoundTest(){
        ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void findByUsernameFoundTest(){

        User user = createUser();
        when(userRepo.findByUsername("Username")).thenReturn(user);
        ResponseEntity<User> response = userController.findByUserName("Username");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
        assertEquals(1L, user.getId());
        assertEquals("Username", user.getUsername());
        assertEquals("Password", user.getPassword());
    }

    @Test
    public void findByUsernameNotFoundTest(){

        ResponseEntity<User> response = userController.findByUserName("Username");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}
