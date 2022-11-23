package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@RunWith(SpringRunner.class)
public class UserControllerTUnit {

    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private CartRepository cartRepositoryMock;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoderMock;
    @InjectMocks
    private UserController userController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void given_validUser_when_createUser_then_returnCreatedUser() {
        when(userRepositoryMock.save(null)).thenReturn(this.buildUser());
        when(bCryptPasswordEncoderMock.encode(any())).thenReturn("HashValue");
        when(cartRepositoryMock.save(any())).thenReturn(new Cart());

        CreateUserRequest request = this.buildCreateUserRequestObject();
        final ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertNotNull(response.getBody()
            .getId());
        assertEquals(response.getBody()
            .getUsername(), "test1");
        assertEquals(response.getBody()
            .getPassword(), "HashValue");
    }

    @Test
    public void given_InvalidUsernameCreateUserRequest_when_createUser_then_returnBadRequestResponse() {
        when(cartRepositoryMock.save(any())).thenReturn(new Cart());

        CreateUserRequest request = this.buildCreateUserRequestObject();
        request.setUsername("bad");
        final ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertTrue(response.getStatusCode().is4xxClientError());
    }
    
    @Test
    public void given_InvalidPasswordCreateUserRequest_when_createUser_then_returnBadRequestResponse() {
        when(cartRepositoryMock.save(any())).thenReturn(new Cart());

        CreateUserRequest request = this.buildCreateUserRequestObject();
        request.setPassword("badc");
        final ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertTrue(response.getStatusCode().is4xxClientError());
    }

    private Object buildUser() {
        return new User(1, "test1", "test1", null);
    }

    private CreateUserRequest buildCreateUserRequestObject() {
        return new CreateUserRequest("test1", "test1", "test1");
    }

}
