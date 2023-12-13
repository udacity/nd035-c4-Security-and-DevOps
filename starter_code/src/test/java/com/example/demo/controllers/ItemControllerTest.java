package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
public class ItemControllerTest {
    private ItemController itemController;
    private final UserRepository userRepo = mock(UserRepository.class);

    private final ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setup() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
    }
    @Test
    public void findItemByIdSuccessTest() {
        Item item1 = newItem(1L, "item_test");
        when(itemRepo.findById(1L)).thenReturn(Optional.of(item1));
        ResponseEntity<Item> responseEntity = itemController.getItemById(1L);

        Item item = responseEntity.getBody();

        assertEquals(responseEntity.getStatusCodeValue(), HttpStatus.OK.value());
        assertNotNull(item);
        assertEquals(item.getId().longValue(), 1L);
    }

    @Test
    public void findItemByIdFailedTest() {
        Item item1 = newItem(1L, "item_test");
        when(itemRepo.findById(1L)).thenReturn(Optional.of(item1));
        ResponseEntity<Item> responseEntity = itemController.getItemById(2L);

        assertEquals(responseEntity.getStatusCodeValue(), HttpStatus.NOT_FOUND.value());
    }
    @Test
    public void findItemByNameSuccessTest() {
        Item item1 = newItem(1L, "item_test_1");
        Item item2 = newItem(1L, "item_test_2");
        Item item3 = newItem(1L, "item_test_2");
        when(itemRepo.findByName("item_test_2")).thenReturn(Lists.list(item2, item3));
        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("item_test_2");


        List<Item> items = responseEntity.getBody();

        assertEquals(responseEntity.getStatusCodeValue(), HttpStatus.OK.value());
        assertNotNull(items);
        assertEquals(items.size(), 2);
    }

    @Test
    public void findItemByNameFailedTest() {
        Item item1 = newItem(1L, "item_test_1");
        Item item2 = newItem(1L, "item_test_2");
        Item item3 = newItem(1L, "item_test_2");
        when(itemRepo.findByName("item_test_2")).thenReturn(Lists.list(item2, item3));
        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("item_test_3");

        assertEquals(responseEntity.getStatusCodeValue(), HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void findAllItemsTest() {
        Item item1 = newItem(1L, "item_test_1");
        Item item2 = newItem(1L, "item_test_2");
        Item item3 = newItem(1L, "item_test_2");
        when(itemRepo.findAll()).thenReturn(Lists.list(item1, item2, item3));
        ResponseEntity<List<Item>> responseEntity = itemController.getItems();

        assertEquals(responseEntity.getStatusCodeValue(), 200);
    }
    private static Item newItem(long id, String name) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        return item;
    }
}
