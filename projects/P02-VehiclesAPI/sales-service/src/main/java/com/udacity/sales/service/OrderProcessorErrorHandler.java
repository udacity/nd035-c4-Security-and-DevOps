package com.udacity.sales.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;

@Service
public class OrderProcessorErrorHandler implements ErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(OrderProcessorErrorHandler.class);

    @Override
    public void handleError(Throwable throwable) {
        log.error("Caught error while processing Order", throwable);
    }
}
