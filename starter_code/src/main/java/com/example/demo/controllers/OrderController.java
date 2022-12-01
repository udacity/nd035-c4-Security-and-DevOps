package com.example.demo.controllers;

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

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

@RestController
@RequestMapping("/order")
public class OrderController {

    private Logger log = LogManager.getLogger(OrderController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;

    /**
     * Submit an order to the database for the given username.
     * @param username the username to submit order for to database.
     * @return the submitted order for the user.
     */
    @PostMapping("/submit/{username}")
    public ResponseEntity<UserOrder> submit(@PathVariable String username) {
        log.info("Start Order Submit for user {}", username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.warn("Order submission for user {} failed due to user not found.");
            return ResponseEntity.notFound()
                .build();
        }
        log.info("Creating order from cart...");
        UserOrder order = UserOrder.createFromCart(user.getCart());
        log.info("Found order from cart with id {}", order.getId());
        log.info("Saving order with id {} to the database...", order.getId());
        orderRepository.save(order);
        return ResponseEntity.ok(order);
    }

    /**
     * Get all orders for the given username.
     * @param username The username associated with the orders.
     * @return List of Order objects for the given user (username).
     */
    @GetMapping("/history/{username}")
    public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
        log.info("Start getOrdersForUser for user {}", username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.warn("Could not find any orders for user {}, user not found exception.", username);
            return ResponseEntity.notFound()
                .build();
        }
        log.info("Found user orders for user {}.", username);
        return ResponseEntity.ok(orderRepository.findByUser(user));
    }
}
