package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.utils.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemControllerTest {
    private ItemController itemController;

    @Autowired
    private  ItemRepository itemRepository ;

    @Before
    public void setUp()  {
        itemRepository.saveAll(CreateMockItems());
        itemController = new ItemController(itemRepository);

    }

    @Test
    public void get_item_by_id(){
        Item item = itemController.getItemById(1L).getBody();
        assert item != null;
        Long id = item.getId();
        Assert.assertEquals(java.util.Optional.of(1L),java.util.Optional.ofNullable(id));
    }

    @Test
    public void get_items(){
        List<Item> items = itemController.getItems().getBody();
        assert items != null;
        assertEquals(10, items.size());

    }

    @Test
    public void get_by_name(){
        List<Item> items = itemController.getItemsByName("Eggs").getBody();
        assert items != null;
        assertEquals(1, items.size());
        assertEquals("Eggs",items.get(0).getName());
        assertEquals(new BigDecimal("1.23"),items.get(0).getPrice());

    }

    public static List<Item>  CreateMockItems(){

        List items = new ArrayList<>();

        items.add(new Item(1L,"Eggs",new BigDecimal("1.23"), "Large Eggs"));
        items.add(new Item(2L,"Ripe Bananas 5 Pack",new BigDecimal("0.79"), "Ripen At Home Bananas 5 Pack"));
        items.add(new Item(3L,"Apple",new BigDecimal("1.60"), "Tesco British Apples Minimum 5 Pack"));
        items.add(new Item(4L,"Sunflower Seeds",new BigDecimal("1.25"), "Tesco Sunflower Seeds 150G"));
        items.add(new Item(5L,"Easter Eggs",new BigDecimal("0.50"), "Milkybar White Chocolate Bunny 17G"));
        items.add(new Item(6L,"Easter Eggs",new BigDecimal("3.00"), "Twix Large Easter Egg 274G"));
        items.add(new Item(7L,"Easter Eggs",new BigDecimal("5.00"), "Maltesers Mint Buttons & Milk Chocolate Egg 274G"));
        items.add(new Item(8L,"Noodles",new BigDecimal("0.75"), "Pot Noodle Pulled Pork 90G"));
        items.add(new Item(9L,"Pet food",new BigDecimal("3.00"), "Purina One Adult Dry Cat Food Chicken & Wholegrain 800G"));
        items.add(new Item(10L,"Tea",new BigDecimal("3.20"), "Pg Tips Pyramid 160 Tea Bags 464G"));

        return items;

    }
}
