package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static com.example.demo.ObjectsTest.*;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemRepository itemRepository;

    @DisplayName("Test get items by username")
    @Test
    public void get_items_by_name(){
        final String username = "user1";
        when(itemRepository.findByName(anyString()))
                .thenReturn(itemListMock());
        final ResponseEntity<List<Item>> response =
                itemController.getItemsByName(username);
        assertNotNull(response);
        final List<Item> items = response.getBody();
        assertNotNull(items);
        assertTrue(items.size() > 0);
    }

    @DisplayName("Test not found items by username")
    @Test
    public void get_items_by_name_not_found(){
        final String username = "user1";
        final ResponseEntity<List<Item>> response =
                itemController.getItemsByName(username);
        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
    }
}