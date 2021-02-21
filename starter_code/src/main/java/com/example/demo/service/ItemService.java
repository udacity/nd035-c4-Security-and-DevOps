package com.example.demo.service;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Optional<Item> findInventoryItemById(Long id){

        return itemRepository.findById(id);

    }

    public List<Item> findInventoryItemByName(String itemName){
        return itemRepository.findByName(itemName);
    }

    public List<Item> findAllInventoryItems(){
        return itemRepository.findAll();
    }

    public void saveAllInventoryItems(List<Item> items) {
        itemRepository.saveAll(items);

    }
}
