package com.example.demo.controller;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    private final ItemRepository itemRepository;

    public CartController(UserRepository userRepository, CartRepository cartRepository, ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
    }

    @PostMapping("/addToCart")
    public ResponseEntity<Cart> addToCart(@RequestBody ModifyCartRequest request) {
        val user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        val item = itemRepository.findById(request.getItemId());
        if (item.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        val cart = user.getCart();
        IntStream.range(0, request.getQuantity())
                .forEach(i -> cart.addItem(item.get()));
        cartRepository.save(cart);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/removeFromCart")
    public ResponseEntity<Cart> removeFromCart(@RequestBody ModifyCartRequest request) {
        val user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        val item = itemRepository.findById(request.getItemId());
        if (item.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        val cart = user.getCart();
        IntStream.range(0, request.getQuantity())
                .forEach(i -> cart.removeItem(item.get()));
        cartRepository.save(cart);
        return ResponseEntity.ok(cart);
    }
}
