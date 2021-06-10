package com.example.demo.controllers;

//import org.slf4j.LoggerFactory;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

//Used this resource to configure console and file logging for log4j2 - https://sematext.com/blog/log4j2-tutorial/
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/api/user")
public class UserController {
	//public static final Logger log = LoggerFactory.getLogger(UserController.class);
	public static final Logger LOGGER = LogManager.getLogger(UserController.class);
	
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
		User user = new User();
		user.setUsername(createUserRequest.getUsername());

		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		if(createUserRequest.getPassword().length()<7 ||
				!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			//System.out.println("Error - Either length is less than 7 or pass and conf pass do not match. Unable to create ",
			//		createUserRequest.getUsername());
			LOGGER.debug("DEBUG: ISSUE WITH PASSWORD SET UP FOR USER = " + createUserRequest.getUsername());

			return ResponseEntity.badRequest().build();
		}

		LOGGER.info("INFO: SUCCESSFUL SET UP FOR USER = " + createUserRequest.getUsername());
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));

		userRepository.save(user);
		return ResponseEntity.ok(user);
	}
	
}
