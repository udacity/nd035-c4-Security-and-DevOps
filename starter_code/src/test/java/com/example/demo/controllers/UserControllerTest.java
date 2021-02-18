package com.example.demo.controllers;

import com.example.demo.utils.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        TestUtils.injectObjects(userController,"userRepository", userRepository);
        TestUtils.injectObjects(userController,"cartRepository", cartRepository);
        TestUtils.injectObjects(userController,"bCryptPasswordEncoder", bCryptPasswordEncoder);

    }
    @Test
    public  void create_user_happy_path() {
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisishashed");

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");

        final ResponseEntity<User> responseEntity = userController.createUser(request);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        User u = responseEntity.getBody();
        assertNotNull(u);

        assertEquals(0,u.getId());
        assertEquals("test",u.getUsername());
        assertEquals("thisishashed",u.getPassword());
    }

    @Test
    public void find_by_id(){
        var id = userController.findById(1l);
        User u = id.getBody();
        assertNotNull(u);
        assertEquals(0,u.getId());
    }


}
