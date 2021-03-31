package com.example.demo.controller;

import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final static Logger log = LoggerFactory.getLogger(OrderController.class);

    public OrderController(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @PostMapping("/submit/{username}")
    public ResponseEntity<UserOrder> submit(@PathVariable String username) {
        log.info(String.format("Submitting order for user %s", username));

        val user = userRepository.findByUsername(username);
        if (user == null) {
            log.error(String.format("Error with order for user %s. User was not found.", username));
            return ResponseEntity.notFound().build();
        }
        val order = UserOrder.createFromCart(user.getCart());
        orderRepository.save(order);

        log.info(String.format("Order for user %s was successfully submitted", username));
        return ResponseEntity.ok(order);
    }

    @GetMapping("/history/{username}")
    public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
        log.info(String.format("Fetching orders for user %s", username));

        val user = userRepository.findByUsername(username);
        if (user == null) {
            log.error(String.format("Error with fetching orders for user %s. User was not found.", username));
            return ResponseEntity.notFound().build();
        }

        log.info(String.format("Successfully fetched orders for user %s", username));
        return ResponseEntity.ok(orderRepository.findByUser(user));
    }
}
