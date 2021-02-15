package com.example.demo.model.persistence;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class CartTest {

    @Test
    public void addItemSetsItemsAndTotalCorrectlyWhenNotInitialized() {
        final Cart cart = new Cart();
        final Item item = new Item();
        item.setPrice(BigDecimal.ONE);

        cart.addItem(item);

        assertEquals(1, cart.getItems().size());
        assertEquals(BigDecimal.ONE, cart.getTotal());
    }

    @Test
    public void removedItemSetsItemsAndTotalCorrectlyWhenNotInitialized() {
        final Cart cart = new Cart();
        final Item item = new Item();
        item.setPrice(BigDecimal.ONE);

        cart.removeItem(item);

        assertEquals(0, cart.getItems().size());
        assertEquals(BigDecimal.valueOf(-1L), cart.getTotal());
    }
}
