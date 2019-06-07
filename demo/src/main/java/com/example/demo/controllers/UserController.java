package com.example.demo.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.exceptions.UserNotFoundException;
import com.example.demo.persistence.model.Cart;
import com.example.demo.persistence.model.User;
import com.example.demo.persistence.repository.UserRepository;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/{id}")
	public Optional<User> getUser(@PathVariable Long id) {
		return userRepository.findById(id);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public User create(@RequestBody User user) {
		user.setCart(new Cart());
		return userRepository.save(user);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		userRepository.findById(id)
			.orElseThrow(UserNotFoundException::new);
		userRepository.deleteById(id);	
	}
	
	
	
}
