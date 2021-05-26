package com.example.demo.controllers;

import com.example.demo.exception.ResourceException;
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
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private Logger logger = LoggerFactory.getLogger(UserController.class);

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
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		Cart cart = new Cart();

		if(createUserRequest.getPassword().length() < 7 ){
			logger.error("Create User failed to include a new User with name: " + user.getUsername() +", password not matching the requirements" );
			throw new ResourceException("Password must be at least 7 characters.");
		}else if (!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			logger.error("Create User failed to include a new User with name: " + user.getUsername() +", passwords not matching" );
			throw new ResourceException("Passwords not matching");
		}
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));

		cartRepository.save(cart);
		user.setCart(cart);
		userRepository.save(user);

		logger.info("Create User succeed to include a new User with name: {}", user.getUsername());
		return ResponseEntity.ok(user);
	}
}
