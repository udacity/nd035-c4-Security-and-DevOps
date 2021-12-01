package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CartControllerTest {

    private CartController cartController;

    private final CartRepository cartRepo = mock(CartRepository.class);

    private final UserRepository userRepo = mock(UserRepository.class);

    private final ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObject(cartController, "cartRepository", cartRepo);
        TestUtils.injectObject(cartController, "userRepository", userRepo);
        TestUtils.injectObject(cartController, "itemRepository", itemRepo);
    }

    //Happy path
    @Test
    public void addToCartTest() throws Exception{
        User mockUser = createMockUser();
        Item mockItem = createMockItem(1L, "Round Widget", new BigDecimal("2.30"),
                "A widget that is round");

        ModifyCartRequest request = createModifyCartRequest(mockUser, mockItem);

        when(userRepo.findByUsername(mockUser.getUsername())).thenReturn(mockUser);
        when(itemRepo.findById(mockItem.getId())).thenReturn(java.util.Optional.of(mockItem));

        final ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart returnedCart = response.getBody();
        assertNotNull(returnedCart);

        List<Item> expectedCartItems = mockUser.getCart().getItems();
        expectedCartItems.add(mockItem);

        for (int i = 1; i<expectedCartItems.size(); i++){
            assertEquals(expectedCartItems.get(i), returnedCart.getItems().get(i));
        }

        assertEquals(mockUser.getCart().getUser(), returnedCart.getUser());
        assertEquals(mockUser.getCart().getTotal(), returnedCart.getTotal());

    }

    //Test user exists before looking up item in ItemRepository
    @Test
    public void addToCartNullUserTest() throws Exception{
        User mockNullUser = new User();
        Item mockNullItem = createMockItem(1L, "Round Widget", new BigDecimal("2.30"),
                "A widget that is round");

        ModifyCartRequest request = createModifyCartRequest(mockNullUser, mockNullItem);

        final ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCartTest() throws Exception{
        User mockUser = createMockUser();
        Item mockItem = createMockItem(1L, "Round Widget", new BigDecimal("2.30"),
                "A widget that is round");

        ModifyCartRequest request = createModifyCartRequest(mockUser, mockItem);

        when(userRepo.findByUsername(mockUser.getUsername())).thenReturn(mockUser);
        when(itemRepo.findById(mockItem.getId())).thenReturn(java.util.Optional.of(mockItem));

        final ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart returnedCart = response.getBody();
        assertNotNull(returnedCart);

        List<Item> expectedCartItems = mockUser.getCart().getItems();
        expectedCartItems.remove(mockItem);

        for (int i = 1; i<expectedCartItems.size(); i++){
            assertEquals(expectedCartItems.get(i), returnedCart.getItems().get(i));
        }

        assertEquals(mockUser.getCart().getUser(), returnedCart.getUser());
        assertEquals(mockUser.getCart().getTotal(), returnedCart.getTotal());

    }

    //Helper methods

    private User createMockUser (){
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("mockUserJeremy");
        mockUser.setCart(createMockCart(mockUser));
        return mockUser;
    }

    private Item createMockItem(Long id, String itemName, BigDecimal itemPrice, String itemDescription){
        Item newItem = new Item();
        newItem.setId(id);
        newItem.setName(itemName);
        newItem.setPrice(itemPrice);
        newItem.setDescription(itemDescription);
        return newItem;
    }

    private List<Item> createMockItemList(){
        Item mockItem1 = createMockItem(1L,"Round Widget", new BigDecimal("2.30"), "A widget that is round" );
        Item mockItem2 = createMockItem(2L,"Square Widget", new BigDecimal("3.15"), "A widget that is square" );

        List<Item> mockItemList = new ArrayList<>();
        mockItemList.add(mockItem1);
        mockItemList.add(mockItem2);

        return mockItemList;
    }

    private Cart createMockCart (User user){
        Cart mockCart = new Cart();

        mockCart.setId(1L);
        mockCart.setItems(createMockItemList());
        mockCart.setUser(user);
        mockCart.setTotal(new BigDecimal("5.45"));
        return mockCart;
    }

    private ModifyCartRequest createModifyCartRequest(User user, Item item){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(user.getUsername());
        modifyCartRequest.setItemId(item.getId());
        modifyCartRequest.setQuantity(1);

        return modifyCartRequest;
    }









}
