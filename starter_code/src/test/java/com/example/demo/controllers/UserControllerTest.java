package com.example.demo.controllers;

import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    @Before
    public void setUp(){

    }

    @Test
    public void create_user_happy_path(){
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test");
    }
}
