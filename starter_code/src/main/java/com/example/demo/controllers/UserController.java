package com.example.demo.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger log = LoggerFactory.getLogger(UserController.class);

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
        if (!(password == null && confirmPassword == null) && password.equals(confirmPassword)) {
            return true;
        }
        // Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@$%^&(){}[]:;<>,.?/~_+-=|\\]).{8,32}$");
        // Matcher matcher = pattern.matcher(password);
        // return matcher.find();
        log.info("Invalid password for password {} and confirmPassword {}...", password, confirmPassword);
        return false;
    }
}
