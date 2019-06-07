package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.persistence.model.Item;
import com.example.demo.persistence.repository.ItemRepository;

@RestController
@RequestMapping("/api/item")
public class ItemController {
	
	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping("/all")
	public Iterable<Item> findAll() {
		return itemRepository.findAll();
	}
	
	@GetMapping("/id/{id}")
	public Optional<Item> getById(@PathVariable Long id) {
		return itemRepository.findById(id);
	}
	
	@GetMapping("/name/{name}")
	public List<Item> getByName(@PathVariable String name) {
		return itemRepository.findByName(name);
	}

}
