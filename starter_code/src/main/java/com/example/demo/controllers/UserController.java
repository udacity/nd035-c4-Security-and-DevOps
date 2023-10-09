package com.example.demo.controllers;

import com.example.demo.constant.AppConstant;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.repositories.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
  private final Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @GetMapping("/id/{id}")
  public ResponseEntity<User> findById(@PathVariable Long id) {
    Optional<User> userOpt = userRepository.findById(id);
    if (!userOpt.isPresent()) {
      logger.error("Not found user with id: {}", id);
      return ResponseEntity.notFound().build();
    }
    logger.info("User found with id: {}", id);
    return ResponseEntity.ok(userOpt.get());
  }

  @GetMapping("/{username}")
  public ResponseEntity<User> findByUserName(@PathVariable String username) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      logger.error("Not found user with username: {}", username);
      return ResponseEntity.notFound().build();
    }
    logger.info("User found with username: {}", username);
    return ResponseEntity.ok(user);
  }

  @PostMapping("/create")
  public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
    if (StringUtils.isEmpty(createUserRequest.getUsername())) {
      logger.error("Username is empty!");
      return ResponseEntity.badRequest().build();
    }
    if (userRepository.existsByUsername(createUserRequest.getUsername())) {
      logger.error("Username is existed!");
      return ResponseEntity.badRequest().build();
    }
    if(createUserRequest.getPassword().length() < AppConstant.PASSWORD_MIN_LENGTH) {
      logger.error("Password to short!");
      return ResponseEntity.badRequest().build();
    }
    if (!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
      logger.error("Confirm password do not match!");
      return ResponseEntity.badRequest().build();
    }

    User user = new User();
    user.setUsername(createUserRequest.getUsername());
    user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));

    Cart cart = cartRepository.save(new Cart());
    user.setCart(cart);

    User userCreated = userRepository.save(user);
    logger.info("User created with username: {}", userCreated.getUsername());

    return ResponseEntity.ok(userCreated);
  }

}
