package com.example.demo.model.persistence;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ItemTest {

    @Test
    public void testEquals_Symmetric() {

        final Item i1 = new Item();
        i1.setId(1L);
        i1.setName("item1");
        i1.setPrice(BigDecimal.ONE);
        i1.setDescription("item1 description");

        final Item i2 = new Item();
        i2.setId(1L);
        i2.setName("item2");
        i2.setPrice(BigDecimal.TEN);
        i2.setDescription("item2 description");

        assertTrue(i1.equals(i2) && i2.equals(i1));
        assertEquals(i1.hashCode(), i2.hashCode());
    }

}
