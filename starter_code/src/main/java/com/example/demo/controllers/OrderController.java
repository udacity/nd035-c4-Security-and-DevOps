package com.example.demo.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	public static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		logger.info("Entered /api/order/submit/{}", username);
		User user = userRepository.findByUsername(username);
		if(user == null) {
			logger.info("submit failure: username {} not found. Exiting.", username);
			return ResponseEntity.notFound().build();
		}
		logger.info("user {} found.", username);

		// Creating order from the user's cart
		UserOrder order = UserOrder.createFromCart(user.getCart());
		logger.info("Order created from the user's cart.");

		orderRepository.save(order);
		logger.info("Submit success: Order was created for user {}, order id {}", username, order.getId());
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		logger.info("Entered /api/order/history/{}", username);
		User user = userRepository.findByUsername(username);
		if(user == null) {
			logger.info("username {} not found. Exiting.", username);
			return ResponseEntity.notFound().build();
		}
		// Getting user's orders and returning them
		logger.info("Returning user {} orders", username);
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
