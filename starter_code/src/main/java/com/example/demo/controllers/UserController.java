package com.example.demo.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	private static final Logger errorLogger = LoggerFactory.getLogger("errors");
	private static final Logger requestLogger = LoggerFactory.getLogger("requests");
	
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
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		log.info("received create user request with " + createUserRequest);
		String requestUsername = createUserRequest.getUsername();
		User existing = userRepository.findByUsername(requestUsername);
		if (existing != null) {
			errorLogger.error("Tried to create user with name " + requestUsername + ", which is already in use!");
			return ResponseEntity.badRequest().build();
		}
		User user = new User();
		user.setUsername(requestUsername);
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		if(createUserRequest.getPassword().length()<7 ||
				!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			//System.out.println("Error - Either length is less than 7 or pass and conf pass do not match. Unable to create ",
			//		createUserRequest.getUsername());
			errorLogger.error("the password provided is too short or did not match the required length!");
			return ResponseEntity.badRequest().build();
		}
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
		user = userRepository.save(user);
		requestLogger.info(MarkerFactory.getMarker("createUser"), "user " + user + " successfully created");
		return ResponseEntity.ok(user);
	}
	
}
