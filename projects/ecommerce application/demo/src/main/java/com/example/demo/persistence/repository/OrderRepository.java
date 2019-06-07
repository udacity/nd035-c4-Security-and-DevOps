package com.example.demo.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.persistence.model.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {
	List<Order> findByUserId(Long userId);
}
