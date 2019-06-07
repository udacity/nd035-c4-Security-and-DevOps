package com.example.demo.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.persistence.model.Item;

public interface ItemRepository extends CrudRepository<Item, Long> {
	List<Item> findByName(String name);

}
