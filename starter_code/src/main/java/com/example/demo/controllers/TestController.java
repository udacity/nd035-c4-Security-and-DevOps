package com.example.demo.controllers;

import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class TestController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;
}
