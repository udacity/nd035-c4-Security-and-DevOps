package uz.jumanazar.ecommerceapp.controllers;

import java.util.List;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

	private static final Logger log = LogManager.getLogger(OrderController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
//		log.info("[order_submit] New request for order submissing for user {}", username);
		try {
			User user = userRepository.findByUsername(username);
			if(user == null) {
				log.error("[order_submit_failure] User not found with username {}", username);
				return ResponseEntity.notFound().build();
			}
			UserOrder order = UserOrder.createFromCart(user.getCart());
			orderRepository.save(order);
			log.info("[order_submit_success] User {}'s order was successfully submitted with details {}", username, order);
			return ResponseEntity.ok(order);
		}catch (Exception e){
			log.error("[order_submit_failure] User not found with username {}, Error: {}", username, e.getMessage());
			return ResponseEntity.status(500).build();
		}
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
//		log.info("[getOrdersForUser] New request getting orders for user {}", username);
		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.error("[getOrdersForUser_failure] User not found with username {}", username);
			return ResponseEntity.notFound().build();
		}
		log.info("[getOrdersForUser_success] New request getting orders for user {}", username);
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
