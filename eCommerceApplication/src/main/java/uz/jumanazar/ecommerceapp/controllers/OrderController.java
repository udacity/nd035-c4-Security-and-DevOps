package uz.jumanazar.ecommerceapp.controllers;

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

import uz.jumanazar.ecommerceapp.model.persistence.User;
import uz.jumanazar.ecommerceapp.model.persistence.UserOrder;
import uz.jumanazar.ecommerceapp.model.persistence.repositories.OrderRepository;
import uz.jumanazar.ecommerceapp.model.persistence.repositories.UserRepository;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	private static final Logger log = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		log.info("[submit] New request for order submissing for user {}", username);
		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.error("[submit] User not found with username {}", username);
			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		log.info("[submit] User {}'s order was successfully submitted with details {}", username, order);
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		log.info("[getOrdersForUser] New request getting orders for user {}", username);
		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.error("[getOrdersForUser] User not found with username {}", username);
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
