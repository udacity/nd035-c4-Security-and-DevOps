package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Field;

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
        userController = new UserController(null, null, null);
        injectObjects(userController, "userRepository", userRepo);
        injectObjects(userController, "cartRepository", cartRepo);
        injectObjects(userController, "bCryptPasswordEncoder", encoder);

        User user = new User();
        Cart cart = new Cart();
        user.setId(0);
        user.setUsername("username");
        user.setPassword("Password@12");
        user.setCart(cart);
        when(userRepo.findByUsername("username")).thenReturn(user);
        when(userRepo.findById(0L)).thenReturn(java.util.Optional.of(user));
        when(userRepo.findByUsername("hoangdq2")).thenReturn(null);

    }

    @Test
    public void createUserSuccess() {
        when(encoder.encode("Password@12")).thenReturn("password");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("username");
        r.setPassword("Password@12");
        r.setConfirmPassword("Password@12");
        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("username", u.getUsername());
        assertEquals("password", u.getPassword());

    }

    @Test
    public void createUserFail() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("username");
        r.setPassword("password");
        r.setConfirmPassword("passwordAgain");
        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void checkLengthRequirement() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("username");
        r.setPassword("passw");
        r.setConfirmPassword("passw");
        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void findUserByNameSuccess() {
        final ResponseEntity<User> response = userController.findByUserName("username");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals("username", u.getUsername());
    }

    @Test
    public void findUserByNameFail() {
        final ResponseEntity<User> response = userController.findByUserName("hoangdq2");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void findUserByIdSuccess() {
        final ResponseEntity<User> response = userController.findById(0L);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());;
    }

    @Test
    public void findUserByIdFail() {
        final ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private void injectObjects(Object target, String fieldName, Object toInject){

        boolean wasPrivate = false;

        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            if (!f.canAccess(target)){
                f.setAccessible(true);
                wasPrivate = true;
            }
            f.set(target, toInject);
            if (wasPrivate){
                f.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
