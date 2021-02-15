package com.example.demo.controller;

import com.example.demo.model.requests.CreateUserRequest;

public class TestUtil {

    public static CreateUserRequest createUserRequest(String username, String password) {
        final CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(username);
        createUserRequest.setPassword(password);
        createUserRequest.setConfirmPassword(password);
        return createUserRequest;
    }
}
