package com.example.demo.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.persistence.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
	User findByUserName(String userName);
}
