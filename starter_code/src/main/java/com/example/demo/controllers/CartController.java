package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/cart")
@Slf4j
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
    public ResponseEntity<Cart> addTocart(@RequestBody ModifyCartRequest request) {
        return process(request, (item, cart) -> cart.addItem(item));
    }

    @PostMapping("/removeFromCart")
    public ResponseEntity<Cart> removeFromcart(@RequestBody ModifyCartRequest request) {
        return process(request, (item, cart) -> cart.removeItem(item));
    }

    private ResponseEntity<Cart> process(ModifyCartRequest request, BiConsumer<Item, Cart> consumer) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
        if (!userOptional.isPresent()) {
            log.error("User {} does not exist", request.getUsername());
            return ResponseEntity.badRequest().build();
        }

        Optional<Item> itemOptional = itemRepository.findById(request.getItemId());
        if (!itemOptional.isPresent()) {
            log.error("Item {} does not exist", request.getItemId());
            return ResponseEntity.badRequest().build();
        }

        Cart cart = userOptional.get().getCart();
        IntStream.range(0, request.getQuantity()).forEach(i -> consumer.accept(itemOptional.get(), cart));
        cartRepository.save(cart);
        return ResponseEntity.ok(cart);
    }
}
