package com.udacity.orders.service;

import com.udacity.orders.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class SalesService {

    private static final Logger log = LoggerFactory.getLogger(SalesService.class);

    private static final String ORDERS_QUEUE_NAME = "ORDERS.Q";

    private final JmsTemplate jmsTemplate;

    public SalesService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    void send(Order order) {
        try {
            jmsTemplate.convertAndSend(ORDERS_QUEUE_NAME, order);
        } catch (JmsException e) {
            log.error("Infrastructure error accessing Message Broker. Make sure the broker is up"
                    + " and the application.properties file is properly configured.", e);
            // ideally we would re-throw the exception and make the transaction on the caller
            // fail
        }
    }
}
