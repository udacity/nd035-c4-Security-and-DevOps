package com.example.demo.controllers;

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

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

@RestController
@RequestMapping("/cart")
public class CartController {

    private Logger log = LogManager.getLogger(CartController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ItemRepository itemRepository;

    @PostMapping("/addToCart")
    public ResponseEntity<Cart> addTocart(@RequestBody ModifyCartRequest request) {
        log.info("Start addToCart for request {}", request);
        User user = userRepository.findByUsername(request.getUsername());

        if (user == null) {
            log.warn("Exception while trying to add to cart because user {} not found", request.getUsername());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .build();
        }

        log.info("Trying to find the item with id {}", request.getItemId());
        Optional<Item> item = itemRepository.findById(request.getItemId());

        if (!item.isPresent()) {
            log.info("Could not find item with id {}, stopping addToCart for user {}", request.getItemId(), request.getUsername());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .build();
        }

        Cart cart = user.getCart();

        IntStream.range(0, request.getQuantity())
            .forEach(i -> cart.addItem(item.get()));

        log.info("Saving cart for user {}...", request.getUsername());
        cartRepository.save(cart);
        log.info("Saved cart to the database for user {}", request.getUsername());
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/removeFromCart")
    public ResponseEntity<Cart> removeFromcart(@RequestBody ModifyCartRequest request) {
        log.info("Start removeFromCart for request {}", request);

        User user = userRepository.findByUsername(request.getUsername());

        if (user == null) {
            log.warn("Exception while trying to removing from cart because user {} not found", request.getUsername());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .build();
        }

        log.info("Trying to find the item with id {}", request.getItemId());
        Optional<Item> item = itemRepository.findById(request.getItemId());
        if (!item.isPresent()) {
            log.info("Could not find item with id {}, stopping addToCart for user {}", request.getItemId(), request.getUsername());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .build();
        }
        Cart cart = user.getCart();
        log.info("Removing items from cart...");
        IntStream.range(0, request.getQuantity())
            .forEach(i -> cart.removeItem(item.get()));
        log.info("Saving cart for user {}...", request.getUsername());
        cartRepository.save(cart);
        return ResponseEntity.ok(cart);
    }

}
