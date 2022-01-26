package com.example.demo.models.persistence;

import com.example.demo.model.persistence.UserOrder;
import org.junit.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserOrderTest {

    @Test
    public void testGetId() {
        UserOrder userOrder = new UserOrder();
        userOrder.setId(1L);
        assertEquals(java.util.Optional.of(1L), java.util.Optional.of(userOrder.getId()));
    }
}