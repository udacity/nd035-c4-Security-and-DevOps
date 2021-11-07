package com.example.demo.model.persistence;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ItemTest {

    @Test
    public void equalsTest(){
        Item item = new Item();
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));

        Item item2 = new Item();
        item2.setName("Square Widget");
        item2.setPrice(BigDecimal.valueOf(1.99));

        assertEquals(item, item);
        assertNotEquals(item, null);
        assertNotEquals(item, new Object());
        assertEquals(item, item2);

        item2.setId(2L);
        assertNotEquals(item, item2);

        item.setId(2L);
        assertEquals(item, item2);
        assertEquals(item.hashCode(), item2.hashCode());

        item.setId(1L);
        assertNotEquals(item, item2);
        assertNotEquals(item.hashCode(), item2.hashCode());
    }
}