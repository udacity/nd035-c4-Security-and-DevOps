package uz.jumanazar.ecommerceapp.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import uz.jumanazar.ecommerceapp.TestUtils;
import uz.jumanazar.ecommerceapp.model.persistence.Item;
import uz.jumanazar.ecommerceapp.model.persistence.repositories.ItemRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private final ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp() throws Exception {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);

        when(itemRepo.findAll()).thenReturn(TestUtils.getItems());
    }

    @Test
    public void getItems() {
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertFalse(items.isEmpty());
    }

    @Test
    public void getItemById() {
        Item item = TestUtils.getItem(1L, "Banana", 3000d, "Fruits");

        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item itemReturned = response.getBody();
        assertNotNull(itemReturned);
        assertEquals(item, itemReturned);
    }

    @Test
    public void getItemsByName() {
        List<Item> itemList = TestUtils.getItems();
        when(itemRepo.findByName("Banana")).thenReturn(itemList);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Banana");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> itemsReturned = response.getBody();
        assertNotNull(itemsReturned);
        assertEquals(itemList, itemsReturned);
    }
}