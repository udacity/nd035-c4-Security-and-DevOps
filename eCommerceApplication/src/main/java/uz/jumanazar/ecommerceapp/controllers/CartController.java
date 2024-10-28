package uz.jumanazar.ecommerceapp.controllers;

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

import uz.jumanazar.ecommerceapp.model.persistence.Cart;
import uz.jumanazar.ecommerceapp.model.persistence.Item;
import uz.jumanazar.ecommerceapp.model.persistence.User;
import uz.jumanazar.ecommerceapp.model.persistence.repositories.CartRepository;
import uz.jumanazar.ecommerceapp.model.persistence.repositories.ItemRepository;
import uz.jumanazar.ecommerceapp.model.persistence.repositories.UserRepository;
import uz.jumanazar.ecommerceapp.model.requests.ModifyCartRequest;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	private static final Logger log = LoggerFactory.getLogger(CartController.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addTocart(@RequestBody ModifyCartRequest request) {
		log.info("[addTocart] New request for adding an item to user's cart with request body {}", request);
		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			log.error("[addTocart] User not found with username {}", request.getUsername());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			log.error("[addTocart] Item not found with ID {}", request.getItemId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.addItem(item.get()));
		cartRepository.save(cart);
		log.info("[addTocart] New item was added to user's cart with item details {}", item.get());
		return ResponseEntity.ok(cart);
	}
	
	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromcart(@RequestBody ModifyCartRequest request) {
		log.info("[removeFromcart] New request for removing an item from user's cart with request body {}", request);
		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			log.error("[removeFromcart] User not found with username {}", request.getUsername());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			log.error("[removeFromcart] Item not found with ID {}", request.getItemId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.removeItem(item.get()));
		cartRepository.save(cart);
		log.info("[removeFromcart] Item was removed from user's cart with item details {}, number of items in cart: {}", item.get(), cart.getItems().size());
		return ResponseEntity.ok(cart);
	}
		
}
