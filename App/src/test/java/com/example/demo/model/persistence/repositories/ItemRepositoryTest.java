package com.example.demo.model.persistence.repositories;


import com.example.demo.model.persistence.Item;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    @Transactional
    public void findByNameTest(){

        Item item = new Item();
        item.setName("Item1");
        item.setDescription("Test Description");
        item.setPrice(BigDecimal.valueOf(10));

        itemRepository.save(item);

        List<Item> foundItems = itemRepository.findByName("Item1");

        Assert.assertNotNull(foundItems);
        Assert.assertEquals("Item1",foundItems.get(0).getName());
        Assert.assertEquals("Test Description",foundItems.get(0).getDescription());
        Assert.assertEquals(BigDecimal.valueOf(10),foundItems.get(0).getPrice());
        Assert.assertNotNull(foundItems.get(0).getId());

        // negative test

        List<Item> unknownItems = itemRepository.findByName("New");
        Assert.assertEquals(0,unknownItems.size());

    }

}
