package uz.jumanazar.ecommerceapp.controllers;

import java.util.Optional;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

	private static final Logger log = LogManager.getLogger(CartController.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addTocart(@RequestBody ModifyCartRequest request) {
//		log.info("[addTocart] New request for adding an item to user's cart with request body {}", request);
		try {
			User user = userRepository.findByUsername(request.getUsername());
			if(user == null) {
				log.error("[addTocart_failure] User not found with username {}", request.getUsername());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			Optional<Item> item = itemRepository.findById(request.getItemId());
			if(!item.isPresent()) {
				log.error("[addTocart_failure] Item not found with ID {}", request.getItemId());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			Cart cart = user.getCart();
			IntStream.range(0, request.getQuantity())
					.forEach(i -> cart.addItem(item.get()));
			cartRepository.save(cart);
			log.info("[addTocart_success] New item was added to user's cart with item_id:{}, " +
					"item_name:{}, item_price:{}", item.get().getId(), item.get().getName(), item.get().getPrice());
			return ResponseEntity.ok(cart);
		}catch (Exception e){
			log.error("[addTocart_failure] Item add to cart failed. Error {} ", e.getMessage());
			return ResponseEntity.status(500).build();
		}
	}
	
	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromcart(@RequestBody ModifyCartRequest request) {
//		log.info("[removeFromcart] New request for removing an item from user's cart with request body {}", request);
		try {
			User user = userRepository.findByUsername(request.getUsername());
			if(user == null) {
				log.error("[removeFromcart_failure] User not found with username {}", request.getUsername());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			Optional<Item> item = itemRepository.findById(request.getItemId());
			if(!item.isPresent()) {
				log.error("[removeFromcart_failure] Item not found with ID {}", request.getItemId());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			Cart cart = user.getCart();
			IntStream.range(0, request.getQuantity())
					.forEach(i -> cart.removeItem(item.get()));
			cartRepository.save(cart);
			log.info("[removeFromcart_success] Item was removed from user's cart with item_id:{}, " +
					"item_name:{}, item_price:{}", item.get().getId(), item.get().getName(), item.get().getPrice());
			return ResponseEntity.ok(cart);
		}catch (Exception e){
			log.error("[removeFromcart_failure] Item remove from cart failed. Error {}", e.getMessage());
			return ResponseEntity.status(500).build();
		}
	}
		
}
