package uz.jumanazar.ecommerceapp.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import uz.jumanazar.ecommerceapp.TestUtils;
import uz.jumanazar.ecommerceapp.model.persistence.Cart;
import uz.jumanazar.ecommerceapp.model.persistence.User;
import uz.jumanazar.ecommerceapp.model.persistence.UserOrder;
import uz.jumanazar.ecommerceapp.model.persistence.repositories.OrderRepository;
import uz.jumanazar.ecommerceapp.model.persistence.repositories.UserRepository;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private final UserRepository userRepo = mock(UserRepository.class);
    private final OrderRepository orderRepo = mock(OrderRepository.class);

    @Before
    public void setUp() throws Exception {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);

        when(userRepo.findByUsername("test")).thenReturn(TestUtils.getUser());
    }

    @Test
    public void submit() {
        UserOrder myOrder = TestUtils.getOrder();
        Cart cart = TestUtils.getCart();

        User user = userRepo.findByUsername("test");
        user.setCart(cart);
        UserOrder order = new UserOrder();
        order.setUser(user);
        when(orderRepo.save(order)).thenReturn(myOrder);
        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        UserOrder orderReturned = response.getBody();
        assertNotNull(orderReturned);
    }

    @Test
    public void getOrdersForUser() {
        User user = userRepo.findByUsername("test");
        assertNotNull(user);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }
}