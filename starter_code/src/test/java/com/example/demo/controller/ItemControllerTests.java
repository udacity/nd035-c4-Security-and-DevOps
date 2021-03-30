package com.example.demo.controller;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ItemControllerTests {

    @Test
    public void testGetItems() {
        // Arrange
        val itemRepositoryMock = mock(ItemRepository.class);
        val items = Arrays.asList(new Item(), new Item());
        when(itemRepositoryMock.findAll()).thenReturn(items);
        val itemController = new ItemController(itemRepositoryMock);

        // Act
        val itemsResponseEntity = itemController.getItems();

        // Assert
        assertEquals(HttpStatus.OK, itemsResponseEntity.getStatusCode());
        assertNotNull(itemsResponseEntity.getBody());
        assertEquals(items, itemsResponseEntity.getBody());

        verify(itemRepositoryMock).findAll();
    }

    @Test
    public void testGetItemByIdHappyPath() {
        // Arrange
        val itemRepositoryMock = mock(ItemRepository.class);
        val item = new Item();
        val itemId = 1L;
        item.setId(itemId);
        when(itemRepositoryMock.findById(itemId)).thenReturn(Optional.of(item));
        val itemController = new ItemController(itemRepositoryMock);

        // Act
        val itemResponseEntity = itemController.getItemById(itemId);

        // Assert
        assertEquals(HttpStatus.OK, itemResponseEntity.getStatusCode());
        assertNotNull(itemResponseEntity.getBody());
        assertEquals(item, itemResponseEntity.getBody());

        verify(itemRepositoryMock).findById(itemId);
    }

    @Test
    public void testGetItemByIdUnhappyPathItemNotFound() {
        // Arrange
        val itemRepositoryMock = mock(ItemRepository.class);
        val itemId = 1L;
        when(itemRepositoryMock.findById(itemId)).thenReturn(Optional.empty());
        val itemController = new ItemController(itemRepositoryMock);

        // Act
        val itemResponseEntity = itemController.getItemById(itemId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, itemResponseEntity.getStatusCode());
        assertNull(itemResponseEntity.getBody());

        verify(itemRepositoryMock).findById(itemId);
    }

    @Test
    public void testGetItemsByNameHappyPath() {
        // Arrange
        val itemRepositoryMock = mock(ItemRepository.class);
        val item = new Item();
        val itemId = 1L;
        val name = "testItem";
        item.setId(itemId);
        item.setName(name);
        val itemList = Collections.singletonList(item);
        when(itemRepositoryMock.findByName(name)).thenReturn(itemList);
        val itemController = new ItemController(itemRepositoryMock);

        // Act
        val itemResponseEntity = itemController.getItemsByName(name);

        // Assert
        assertEquals(HttpStatus.OK, itemResponseEntity.getStatusCode());
        assertNotNull(itemResponseEntity.getBody());
        assertEquals(itemList, itemResponseEntity.getBody());

        verify(itemRepositoryMock).findByName(name);
    }

    @Test
    public void testGetItemsByNameUnhappyPath() {
        // Arrange
        val itemRepositoryMock = mock(ItemRepository.class);
        val name = "testItem";
        when(itemRepositoryMock.findByName(name)).thenReturn(null);
        val itemController = new ItemController(itemRepositoryMock);

        // Act
        val itemResponseEntity = itemController.getItemsByName(name);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, itemResponseEntity.getStatusCode());
        assertNull(itemResponseEntity.getBody());

        verify(itemRepositoryMock).findByName(name);
    }

}
