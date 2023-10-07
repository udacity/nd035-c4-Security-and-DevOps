package com.example.demo.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.configs.JwtTokenProvider;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.UserLoginRequest;

@RestController
public class HomeController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    private static final Logger LOGGER = LogManager.getLogger(HomeController.class);

    @GetMapping("/")
    public String home() {
        return "Hello!!";
    }

    @PostMapping("/login")
    public String authenticateUser(@Validated @RequestBody UserLoginRequest loginRequest) {
        LOGGER.info("Logging user " + loginRequest.getUserName());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken((User) authentication.getPrincipal());
        return jwt;
    }

    @GetMapping("/me")
    public ResponseEntity<User> getInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User pricipal = (User) auth.getPrincipal();
        return ResponseEntity.ok(pricipal);
    }
}
