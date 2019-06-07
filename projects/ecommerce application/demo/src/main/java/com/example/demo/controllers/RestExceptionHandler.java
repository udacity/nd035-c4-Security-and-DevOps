package com.example.demo.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.demo.model.exceptions.CartNotFoundException;
import com.example.demo.model.exceptions.ItemNotFoundException;
import com.example.demo.model.exceptions.UserNotFoundException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
 
    @ExceptionHandler({ UserNotFoundException.class, ItemNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(
      Exception ex, WebRequest request) {
    	if(ex instanceof UserNotFoundException) {
    		return handleExceptionInternal(ex, "User not found", 
    		          new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    	} else if(ex instanceof ItemNotFoundException) {
    		return handleExceptionInternal(ex, "Item not found", 
  		          new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    	} else if(ex instanceof CartNotFoundException) {
    		return handleExceptionInternal(ex, "Cart not found", 
    		          new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    	}
    	return handleExceptionInternal(ex, "Not found", 
		          new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
