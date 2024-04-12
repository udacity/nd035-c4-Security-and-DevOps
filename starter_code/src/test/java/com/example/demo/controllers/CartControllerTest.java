package com.example.demo.controllers;

import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

import static org.mockito.Mockito.mock;

public class CartControllerTest {
    private CartController cartController;
    private CartRepository cartRepository = mock(CartRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
}
