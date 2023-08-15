package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/user")
public class UserController {
	public static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		logger.info("Entered /api/user/id/{}", id);
		ResponseEntity<User> response = ResponseEntity.of(userRepository.findById(id));
		if (response.getStatusCodeValue() == 200) {
			logger.info("Success finding user id at /api/user/id/{}", id);
		} else {
			logger.error("Could not find user with id {}", id);
		}
		logger.info("Exiting /api/user/id/{}", id);
		return response;
	}

	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		logger.info("Entered /api/user/{}", username);
		User user = userRepository.findByUsername(username);
		if (user == null) {
			logger.error("findByUserName {}: Failed.", username);
		} else {
			logger.info("findByUserName {}: Success.", username);
		}
		logger.info("Exiting /api/user/{}", username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}

	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		logger.info("Entered /api/user/create");
		User user = new User();
		String username = createUserRequest.getUsername();
		user.setUsername(username);
		logger.info("Username set to {}", username);

		// Setting password
		String password = createUserRequest.getPassword();
		String passwordConfirm = createUserRequest.getConfirmPassword();
		if(password == null ||  password.length() < 5 || !password.equals(passwordConfirm)){
			logger.info("User passwords did not match or meet password criteria. Exiting create user.");
			return ResponseEntity.badRequest().build();
		}
		user.setPassword(bCryptPasswordEncoder.encode(password));
		logger.info("Password successfully set.");

		// Creating user's cart
		Cart cart = new Cart();
		logger.info("User cart created.");
		cartRepository.save(cart);
		logger.info("User cart saved.");
		user.setCart(cart);
		logger.info("User cart set to the created Cart.");

		// Saving the user
		userRepository.save(user);
		logger.info("Success creating user with username {} and id {}", username, user.getId());
		return ResponseEntity.ok(user);
	}

}
