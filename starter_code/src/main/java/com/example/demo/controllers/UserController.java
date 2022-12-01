package com.example.demo.controllers;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    private Logger log = LogManager.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserRepository userRepository, CartRepository cartRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        super();
        log.debug("Instanciating UserController RestController...");
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        log.debug("Instanciated UserController succesfully");
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        log.info("Start findById for user id {}", id);
        Optional<User> user = this.userRepository.findById(id);

        if (!user.isPresent()) {
            log.info("No user found for the given id {}...");
            return ResponseEntity.badRequest()
                .build();
        }

        User foundUser = user.get();
        log.info("Found user for the given id: username: {}", foundUser.getUsername());
        return ResponseEntity.of(user);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        return user == null ? ResponseEntity.notFound()
            .build() : ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {

        log.info("Start createUser for request with username: {}", createUserRequest.getUsername());

        User user = new User();
        Cart cart = new Cart();

        user.setUsername(createUserRequest.getUsername());
        cartRepository.save(cart);
        user.setCart(cart);

        if (!isCreateUserRequestValid(createUserRequest)) {
            return ResponseEntity.badRequest()
                .build();
        }
        log.info("Hashing password for storage...");
        user.setPassword(this.bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
        log.info("Saving user {} to the database...", createUserRequest.getUsername());
        this.userRepository.save(user);
        log.info("Successfully saved user {} to the database.", user.getUsername());
        return ResponseEntity.ok(user);
    }

    private boolean isCreateUserRequestValid(CreateUserRequest createUserRequest) {

        if (isValidUsername(createUserRequest.getUsername()) && this.isValidPassword(createUserRequest.getPassword(), createUserRequest.getConfirmPassword())) {
            return true;
        }
        log.info("Bad createUserRequest for username {}", createUserRequest.getUsername());
        return false;
    }

    private boolean isValidUsername(String username) {
        if (username != null && username.length() >= 5) {
            return true;
        }
        log.info("Invalid username format for username {}...", username);
        return false;
    }

    private boolean isValidPassword(String password, String confirmPassword) {

        boolean isValid = false;

        if (!(password == null && confirmPassword == null) && password.equals(confirmPassword)) {
            isValid = true;
        }
        Pattern pattern = Pattern.compile("(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$");
        Matcher matcher = pattern.matcher(password);
        isValid = matcher.find();
        return isValid;
    }
}
