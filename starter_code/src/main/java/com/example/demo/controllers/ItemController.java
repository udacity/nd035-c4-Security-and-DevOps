package com.example.demo.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RestController
@RequestMapping("/item")
public class ItemController {

    private Logger log = LogManager.getLogger(ItemController.class);

    @Autowired
    private ItemRepository itemRepository;

    /**
     * Gets all the items currently saved in the underlying database.
     * @return List of Item objects fetched from the database.
     */
    @GetMapping
    public ResponseEntity<List<Item>> getItems() {
        log.info("Start getItems (ALL)...");
        return ResponseEntity.ok(itemRepository.findAll());
    }

    /**
     * Get an Item object by its id.
     * @param id id value of the object
     * @return Item object found in the database by its id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        log.info("Start get Item by Id for the given id {}", id);
        return ResponseEntity.of(itemRepository.findById(id));
    }

    /**
     * Gets list of Item objects by a given name.
     * @param name item names to filter search
     * @return List of Item objects found for given name.
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
        log.info("Start getItemsByName for given item name {}", name);
        List<Item> items = itemRepository.findByName(name);
        log.info("Found {} items for the given item name {}", items.size(), name);
        return items == null || items.isEmpty() ? ResponseEntity.notFound()
            .build() : ResponseEntity.ok(items);
    }
}