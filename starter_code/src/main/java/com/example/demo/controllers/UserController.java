package com.example.demo.controllers;

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
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserRepository userRepository, CartRepository cartRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        super();
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.of(userRepository.findById(id));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        return user == null ? ResponseEntity.notFound()
            .build() : ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        User user = new User();
        Cart cart = new Cart();

        user.setUsername(createUserRequest.getUsername());
        cartRepository.save(cart);
        user.setCart(cart);

        if (isCreateUserRequestValid(createUserRequest)) {
            user.setPassword(this.bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
            this.userRepository.save(user);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest()
            .build();
    }

    private boolean isCreateUserRequestValid(CreateUserRequest createUserRequest) {
        Boolean isValid = false;
        if (isValidUsername(createUserRequest.getUsername()) && this.isValidPassword(createUserRequest.getPassword(), createUserRequest.getConfirmPassword())) {
            isValid = true;
        }
        return isValid;
    }

    private boolean isValidUsername(String username) {
        if (username != null && username.length() >= 5) {
            return true;
        }
        return false;
    }

    private boolean isValidPassword(String password, String confirmPassword) {
        if (password == null && confirmPassword == null && !password.equals(confirmPassword)) {
            return false;
        }
        // Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@$%^&(){}[]:;<>,.?/~_+-=|\\]).{8,32}$");
        // Matcher matcher = pattern.matcher(password);
        // return matcher.find();
        return true;
    }
}
