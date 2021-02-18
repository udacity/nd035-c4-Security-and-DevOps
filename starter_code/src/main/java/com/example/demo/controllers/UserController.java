package com.example.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private final UserRepository userRepository;
	
	private final CartRepository cartRepository;

	public  final BCryptPasswordEncoder bCryptPasswordEncoder;

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	public UserController() {
		userRepository =null;
		cartRepository = null;
		bCryptPasswordEncoder = null;

	}

	public UserController(UserRepository userRepository, CartRepository cartRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
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
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		var userName =createUserRequest.getUsername();
		user.setUsername(userName);
		Cart cart = new Cart();
		log.info("user name: "+ userName);
		cartRepository.save(cart);
		user.setCart(cart);

		if(createUserRequest.getPassword().length() < 7
				|| !createUserRequest.getConfirmPassword()
				.equals(createUserRequest.getPassword())){
			ResponseEntity.badRequest().build();

		}
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getConfirmPassword()));
		final var save = userRepository.save(user);

		return ResponseEntity.ok(save);
	}
	
}
