package uz.jumanazar.ecommerceapp.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import uz.jumanazar.ecommerceapp.TestUtils;
import uz.jumanazar.ecommerceapp.model.persistence.Cart;
import uz.jumanazar.ecommerceapp.model.persistence.Item;
import uz.jumanazar.ecommerceapp.model.persistence.User;
import uz.jumanazar.ecommerceapp.model.persistence.repositories.CartRepository;
import uz.jumanazar.ecommerceapp.model.persistence.repositories.ItemRepository;
import uz.jumanazar.ecommerceapp.model.persistence.repositories.UserRepository;
import uz.jumanazar.ecommerceapp.model.requests.ModifyCartRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private final CartRepository cartRepo = mock(CartRepository.class);
    private final UserRepository userRepo = mock(UserRepository.class);
    private final ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp() throws Exception {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);

        when(userRepo.findByUsername("test")).thenReturn(TestUtils.getUser());
    }


    @Test
    public void addTocartWorks() {
        ModifyCartRequest req = new ModifyCartRequest();
        req.setUsername("test");
        req.setQuantity(1);
        req.setItemId(1);

        User testUser = TestUtils.getUser();
        Item itemApple = TestUtils.getItem(1L, "Apple", 2000d, "Food");

        List<Item> itemAdded = TestUtils.addToItems(itemApple);
        Cart cart = TestUtils.getCart();
        cart.setUser(testUser);
        cart.setItems(itemAdded);
        BigDecimal total = cart.getItems().stream()
                        .map(Item::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotal(total);

        User userReturned = userRepo.findByUsername(req.getUsername());
        assertNotNull(userReturned);

        when(itemRepo.findById(req.getItemId())).thenReturn(Optional.of(itemApple));
        Optional<Item> item = itemRepo.findById(req.getItemId());
        assertTrue(item.isPresent());
        Item itemReturned = item.get();
        assertEquals(itemApple.getId(), itemReturned.getId());
        assertEquals(itemApple.getPrice(), itemReturned.getPrice());
        assertEquals(itemApple.getName(), itemReturned.getName());

        assertNotNull(userReturned.getCart());
        Cart userCart = userReturned.getCart();
        IntStream.range(0, req.getQuantity())
                .forEach(i -> userCart.addItem(itemReturned));
        ResponseEntity<Cart> response = cartController.addTocart(req);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart cart1 = response.getBody();
        assertNotNull(cart1);
        assertNotNull(cart1.getItems());
        assertNotNull(cart1.getTotal());
    }

    @Test
    public void removeFromcartWorks() {
        ModifyCartRequest req = new ModifyCartRequest();
        req.setItemId(1);
        req.setQuantity(1);
        req.setUsername("test");
        User user = userRepo.findByUsername(req.getUsername());
        assertNotNull(user);
        when(itemRepo.findById(req.getItemId())).thenReturn(Optional.of(TestUtils.getItem(1L, "Apple", 2000d, "Food")));
        Optional<Item> item = itemRepo.findById(req.getItemId());
        assertNotNull(item);
        ResponseEntity<Cart> response = cartController.removeFromcart(req);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
    }
}