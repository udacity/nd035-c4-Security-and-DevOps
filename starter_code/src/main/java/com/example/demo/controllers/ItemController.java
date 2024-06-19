package com.example.demo.controllers;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RestController
@RequestMapping("/api/item")
@Slf4j
public class ItemController {

	private static final Logger log = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		return ResponseEntity.ok(itemRepository.findAll());
	}

	@PostMapping("/create")
	public ResponseEntity<Item> createItem(@RequestBody Item item) {
		if (item != null && item.getName() != null) {
			itemRepository.save(item);
			log.info("create Item successfully: item = {}", item);
			return ResponseEntity.ok(item);
		}
		log.error("Item data is error: item = {}", item);
		return ResponseEntity.badRequest().body(null);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		if (!itemRepository.findById(id).isPresent()) {
			log.error("Item can not be found by Id {}", id);
			return ResponseEntity.notFound().build();
		}
		log.info("Item can be found by Id {}", id);
		return ResponseEntity.of(itemRepository.findById(id));
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		List<Item> items = itemRepository.findByName(name);
		if (items == null || items.isEmpty()) {
			log.error("Item can not be found by name {}", name);
			return  ResponseEntity.notFound().build();
		}
		log.info("Item can be found by name {}", name);
		return ResponseEntity.ok(items);
	}
	
}
