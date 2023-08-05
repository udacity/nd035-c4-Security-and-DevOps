package com.example.demo.controllers;
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
@RequestMapping("/api/user")
public class UserController {

	Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null){
			logger.error("User not found");
			return ResponseEntity.notFound().build();
		}else{
			return  ResponseEntity.ok(user);
		}

	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {

		try {
			User user = new User();
			user.setUsername(createUserRequest.getUsername());
			if (createUserRequest.getPassword().length() < 7 ||
					!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
				logger.error("Password too short or password and password confirmation do not match.");
				return ResponseEntity.badRequest().build();
			}
			user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));

			Cart cart = new Cart();
			cartRepository.save(cart);
			user.setCart(cart);

			userRepository.save(user);
			logger.info("User successfully created: Name:" + user.getUsername());
			return ResponseEntity.ok(user);

		}catch (Exception e){

			logger.error("Error while creating User");
			return ResponseEntity.badRequest().build();
		}
	}
	
}
