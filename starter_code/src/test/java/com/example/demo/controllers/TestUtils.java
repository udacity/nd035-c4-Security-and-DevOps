package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;

import java.math.BigDecimal;

public class TestUtils {

    public static final String WIDGET = "Widget";

    public static Item getItem01() {
        Item item = new Item();
        item.setId(0L);

        item.setName(WIDGET);
        item.setPrice(new BigDecimal("3.21"));

        item.setDescription("");
        return item;
    }

    public static Item getItem02() {
        Item item = new Item();
        item.setId(1L);

        item.setName("Widget");

        item.setPrice(new BigDecimal("1.23"));

        item.setDescription("");
        return item;
    }
}
