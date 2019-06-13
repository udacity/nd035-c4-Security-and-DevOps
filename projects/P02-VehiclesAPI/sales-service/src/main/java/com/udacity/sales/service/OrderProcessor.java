package com.udacity.sales.service;

import com.udacity.orders.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessor {

    private static final Logger log = LoggerFactory.getLogger(OrderProcessor.class);

    private static final String ORDERS_QUEUE_NAME = "ORDERS.Q";

    @JmsListener(destination = ORDERS_QUEUE_NAME, containerFactory = "jmsFactory")
    public void onOrder(Order order) {
        log.info("Processing Order {}", order);
    }
}
