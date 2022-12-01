package com.example.demo.model.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;

import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Method defined to find Cart object by User object.
     * @param user Used to find related Carts. 
     * @return Cart found for given User object.
     */
    Cart findByUser(User user);
}