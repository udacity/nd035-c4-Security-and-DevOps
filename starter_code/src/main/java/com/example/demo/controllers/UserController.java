package com.example.demo.controllers;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		LOGGER.info("Find user by id {}", id);
		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) {
			LOGGER.error("Error user not found.");
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(user.get());

	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		LOGGER.info("Find user by username {}", username);
		User user = userRepository.findByUsername(username);
		if (Objects.isNull(user)) {
			LOGGER.error("Error user not found.");
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(user);

	}

	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		LOGGER.debug("Creating user {}", createUserRequest.getUsername());
		User user = new User();
		try {
			user.setUsername(createUserRequest.getUsername());
			Cart cart = new Cart();
			cart.setItems(Collections.emptyList());
			cart = cartRepository.save(cart);
			user.setCart(cart);
			if (!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
				LOGGER.error("Error with user password. Cannot create user {}", createUserRequest.getUsername());
				return ResponseEntity.badRequest().build();
			}
			user.setPassword(createUserRequest.getPassword());
			user = userRepository.save(user);
			LOGGER.info("user: {} - password: {} - cast: {}", user.getUsername(), user.getPassword(), user.getCart().toString());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return ResponseEntity.ok(user);
	}
	
}
