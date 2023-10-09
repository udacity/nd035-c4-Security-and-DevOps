package com.example.demo.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/api/order")
public class OrderController {
  private final Logger logger = LoggerFactory.getLogger(OrderController.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private OrderRepository orderRepository;

  @PostMapping("/submit/{username}")
  public ResponseEntity<UserOrder> submit(@PathVariable String username) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      logger.error("Can not submit order. Not found user with username: {}", username);
      return ResponseEntity.notFound().build();
    }
    UserOrder order = UserOrder.createFromCart(user.getCart());
    UserOrder orderSubmitted = orderRepository.save(order);
    logger.info("Order id {} has been submitted", orderSubmitted.getId());
    return ResponseEntity.ok(orderSubmitted);
  }

  @GetMapping("/history/{username}")
  public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      logger.error("Can not retrieve order history. Not found user with username: {}", username);
      return ResponseEntity.notFound().build();
    }
    List<UserOrder> orderList = orderRepository.findByUser(user);
    logger.info("Retrieving {} orders of user {} was found", orderList.size(), username);
    return ResponseEntity.ok(orderList);
  }
}
