package com.udacity.orders.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED, reason = "Order cannot be canceled")
class CancelStatusException extends RuntimeException {

    CancelStatusException(String message) {
        super(message);
    }
}
