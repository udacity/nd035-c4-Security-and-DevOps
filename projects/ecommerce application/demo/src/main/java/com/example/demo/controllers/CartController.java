package com.example.demo.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.exceptions.CartNotFoundException;
import com.example.demo.model.exceptions.ItemNotFoundException;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.persistence.model.Cart;
import com.example.demo.persistence.model.Item;
import com.example.demo.persistence.repository.CartRepository;
import com.example.demo.persistence.repository.ItemRepository;
import com.example.demo.persistence.repository.UserRepository;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	@GetMapping("/{id}")
	public Optional<Cart> getById(@PathVariable Long id) {
		return cartRepository.findById(id);
	}
	
	@GetMapping("/user/{userId}")
	public Optional<Cart> getByUser(@PathVariable Long userId) {
		return cartRepository.findByUserId(userId);
	}
	
	@RequestMapping(
			value = "/addItem", 
			headers = "Accept=application/json",
			method = RequestMethod.POST)
	public Cart addToCart(@RequestBody ModifyCartRequest request) {
		Cart cart = cartRepository.findById(request.getCartId())
				.orElseThrow(CartNotFoundException::new);
		Item item = itemRepository.findById(request.getItemId())
				.orElseThrow(ItemNotFoundException::new);
		cart.addItem(item);
		cartRepository.save(cart);
		return cart;
	}
	
	@RequestMapping(
			value = "/removeItem",
			headers = "Accept=application/json",
			method = RequestMethod.POST)
	public Cart removeFromCart(@RequestBody ModifyCartRequest request) {
		Cart cart = cartRepository.findById(request.getCartId())
				.orElseThrow(CartNotFoundException::new);
		Item item = itemRepository.findById(request.getItemId())
				.orElseThrow(ItemNotFoundException::new);
		cart.removeItem(item);
		cartRepository.save(cart);
		return cart;
	}
	
}
