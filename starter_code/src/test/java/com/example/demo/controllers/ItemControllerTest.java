package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ItemControllerTest {

    private ItemController itemController;

    private final ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepo);
    }

    @Test
    public void getItemsTest(){
        List<Item> mockItemList = createMockItemList();

        when(itemRepo.findAll()).thenReturn(mockItemList);

        final ResponseEntity<List<Item>> responseEntity = itemController.getItems();

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        List<Item> returnedItemList = responseEntity.getBody();

        assertNotNull(returnedItemList);
        assertEquals(mockItemList.size(), returnedItemList.size());

        for (int i=0; i < mockItemList.size(); i++){
            assertEquals(mockItemList.get(i).getName(), returnedItemList.get(i).getName());
            assertEquals(mockItemList.get(i).getPrice(), returnedItemList.get(i).getPrice());
            assertEquals(mockItemList.get(i).getDescription(), returnedItemList.get(i).getDescription());
        }
    }

    @Test
    public void getItemByIdTest(){
        Item mockItem = createMockItem(1L,"Round Widget", new BigDecimal("2.30"),
                "A widget that is round" );

        when(itemRepo.findById(mockItem.getId())).thenReturn(java.util.Optional.of(mockItem));

        final ResponseEntity<Item> responseEntity = itemController.getItemById(mockItem.getId());

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        Item returnedItem = responseEntity.getBody();

        assertNotNull(returnedItem);
        assertEquals(mockItem.getName(),returnedItem.getName());
        assertEquals(mockItem.getPrice(), returnedItem.getPrice());
        assertEquals(mockItem.getDescription(), returnedItem.getDescription());

    }

    @Test
    public void getItemByNameTest(){
        List<Item> mockItemList = createMockItemList();

        when(itemRepo.findByName("Square Widget")).thenReturn(mockItemList);

        final ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("Square Widget");

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        List<Item> returnedItemList = responseEntity.getBody();

        assertNotNull(returnedItemList);
        assertEquals(mockItemList.get(1).getName(),returnedItemList.get(1).getName());
        assertEquals(mockItemList.get(1).getPrice(), returnedItemList.get(1).getPrice());
        assertEquals(mockItemList.get(1).getDescription(), returnedItemList.get(1).getDescription());

    }

    //Helper methods
    private Item createMockItem(Long id, String itemName, BigDecimal itemPrice, String itemDescription){

        Item newItem = new Item();
        newItem.setId(id);
        newItem.setName(itemName);
        newItem.setPrice(itemPrice);
        newItem.setDescription(itemDescription);
        return newItem;

    }

    private List<Item> createMockItemList(){

        List<Item> mockItemList = new ArrayList<>();
        Item mockItem1 = createMockItem(1L,"Round Widget", new BigDecimal("2.30"),
                "A widget that is round" );
        Item mockItem2 = createMockItem(2L,"Square Widget",
                new BigDecimal("3.15"), "A widget that is square" );


        mockItemList.add(mockItem1);
        mockItemList.add(mockItem2);

        return mockItemList;

    }


}
