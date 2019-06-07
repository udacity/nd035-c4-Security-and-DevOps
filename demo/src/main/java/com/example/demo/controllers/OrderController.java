package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.exceptions.CartNotFoundException;
import com.example.demo.persistence.model.Cart;
import com.example.demo.persistence.model.Order;
import com.example.demo.persistence.repository.OrderRepository;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private OrderRepository orderRepository;
	
	
	@PostMapping("/submit")
	@ResponseStatus(HttpStatus.CREATED)
	public Order submit(@RequestBody Cart cart) {
		if(cart != null && cart.getItems() != null) {
			Order order = new Order();
			order.setItems(cart.getItems());
			order.setTotal(cart.getTotal());
			order.setUser(cart.getUser());
			orderRepository.save(order);
			return order;
		}
		throw new CartNotFoundException();
	}
	
	
}
