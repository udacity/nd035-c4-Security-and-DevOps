package com.example.demo.models.persistence;

import com.example.demo.model.persistence.Item;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    @Test
    public void testEquals() {
        Item item = com.example.demo.controllers.TestUtils.getItem01();
        assertNotEquals(item, null);
        String str = "ABC";
        assertNotEquals(str, item);
        Item item2 = com.example.demo.controllers.TestUtils.getItem02();
        assertNotEquals(item, item2);
        item.setId(null);
        assertNotEquals(item, item2);
    }

    @Test
    public void testEquals_Symmetric() {
        Item item1 = new Item();
        item1.setId(0L);
        Item item2 = new Item();
        item2.setId(0L);
        assertTrue(item1.equals(item2) && item2.equals(item1));
        assertEquals(item2.hashCode(), item1.hashCode());
    }}