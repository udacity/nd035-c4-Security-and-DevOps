package com.example.demo.controllers;

import java.util.Optional;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	public static final Logger logger = LoggerFactory.getLogger(CartController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addToCart(@RequestBody ModifyCartRequest request) {
		logger.info("User entered endpoint /api/cart/addToCart");
		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			logger.info("addToCart failed: user {} not found. Exiting.", request.getUsername());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		// Fetching the item from the request
		logger.info("Fetching the items.");
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			logger.info("addToCart failed: item id {} not found. Exiting.", request.getItemId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		logger.info("Item fetched. Adding to the cart.");
		Cart cart = user.getCart();
		logger.info("cart id {} has {} previous item(s)", cart.getId(), cart.getItems().size());
		logger.info("Adding {} new items to cart id {}", request.getQuantity(), cart.getId());

		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.addItem(item.get()));

		logger.info("Items added. Cart size is now {}", cart.getItems().size());
		cartRepository.save(cart);
		logger.info("addToCart success. Returning cart");
		return ResponseEntity.ok(cart);
	}
	
	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromCart(@RequestBody ModifyCartRequest request) {
		logger.info("User entered endpoint /api/cart/removeFromCart");
		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			logger.info("removeFromCart failed: User {} not found. Exiting.", request.getUsername());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		// Fetching the item from the request
		logger.info("Fetching the items.");
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			logger.info("removeFromCart failed: item id {} not found. Exiting.", request.getItemId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		logger.info("Item fetched. Adding to the cart.");
		Cart cart = user.getCart();
		logger.info("cart id {} has {} previous items", cart.getId(), cart.getItems().size());
		logger.info("Removing {} items from cart id {}", request.getQuantity(), cart.getId());
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.removeItem(item.get()));
		logger.info("Items removed. Cart size is now {}", cart.getItems().size());
		cartRepository.save(cart);
		logger.info("removeFromCart success. Returning cart");
		return ResponseEntity.ok(cart);
	}
		
}
