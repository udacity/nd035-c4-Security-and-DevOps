package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RestController
@RequestMapping("/api/item")
public class ItemController {
  private final Logger logger = LoggerFactory.getLogger(ItemController.class);

  @Autowired
  private ItemRepository itemRepository;

  @GetMapping
  public ResponseEntity<List<Item>> getItems() {
    logger.info("Retrieving all items");
    return ResponseEntity.ok(itemRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Item> getItemById(@PathVariable Long id) {
    Optional<Item> itemOpt = itemRepository.findById(id);
    if (!itemOpt.isPresent()) {
      logger.error("Not found item with id: {}", id);
      return ResponseEntity.notFound().build();
    }
    logger.info("Item found with id: {}", id);
    return ResponseEntity.ok(itemOpt.get());
  }

  @GetMapping("/name/{name}")
  public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
    List<Item> items = itemRepository.findByName(name);
    if (items == null || items.isEmpty()) {
      logger.error("Not found items with name: {}", name);
      return ResponseEntity.notFound().build();
    }
    logger.info("Retrieving {} items with name {} was found", items.size(), name);
    return ResponseEntity.ok(items);
  }

}
