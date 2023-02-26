package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/user"})
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController() {
    }

    @GetMapping({"/id/{id}"})
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.of(this.userRepository.findById(id));
    }

    @GetMapping({"/{username}"})
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        User user = this.userRepository.findByUsername(username);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @PostMapping({"/create"})
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        Cart cart = new Cart();
        this.cartRepository.save(cart);
        user.setCart(cart);
        if (createUserRequest.getPassword().length() >= 7 && createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
            user.setPassword(this.bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
            this.userRepository.save(user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
