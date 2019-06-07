package com.example.demo.persistence.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.persistence.model.Cart;

public interface CartRepository extends CrudRepository<Cart, Long> {
	Optional<Cart> findByUserId(Long userId);

}
