package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
	Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> createUser(@RequestBody CreateUserRequest createUserRequest) {
		logger.info("User creation - verifying step started");
		if (createUserRequest.getPassword().length() < 8) {
			logger.info("User creation - verifying step - Password must be at least 8 characters long");
			logger.info("User creation - verifying step ended");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Password must be at least 8 characters long");
		}

		if (!createUserRequest.getPassword().equals(createUserRequest.getPasswordConfirmation())) {
			logger.info("User creation - verifying step - Password and password confirmation mismatch");
			logger.info("User creation - verifying step ended");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Password and password confirmation mismatch");
		}

		User exsitedUser = userRepository.findByUsername(createUserRequest.getUsername());
		if (exsitedUser != null) {
			logger.info("User creation - verifying step - Username is already existed");
			logger.info("User creation - verifying step ended");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Username is already existed");
		}
		logger.info("User creation - verifying step ended");

		logger.info("User creation - creation step started");
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		userRepository.save(user);
		logger.info("User creation - creation step successfully");
		logger.info("User creation - creation step ended");
		return ResponseEntity.ok(user);
	}
}
