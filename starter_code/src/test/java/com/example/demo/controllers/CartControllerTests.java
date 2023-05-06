package com.example.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTests {
	@Mock
	private UserRepository userRepository;

	@Mock
	private ItemRepository itemRepository;

	@Mock
	private CartRepository cartRepository;

	@InjectMocks
	private CartController cartController;

	@Test
	public void testAddToCart() {
		// Arrange
		String username = "testuser";
		Long itemId = 1L;
		int quantity = 1;

		User user = new User();
		user.setId(1L);
		user.setUsername(username);

		Item item = new Item();
		item.setId(itemId);
		item.setName("Test Item");
		item.setDescription("Test Item Description");
		item.setPrice(new BigDecimal("10.00"));

		ModifyCartRequest request = new ModifyCartRequest();
		request.setUsername(username);
		request.setItemId(itemId);
		request.setQuantity(quantity);

		Cart cart = new Cart();
		cart.setId(1L);
		cart.setUser(user);

		user.setCart(cart);

		when(userRepository.findByUsername(username)).thenReturn(user);
		when(itemRepository.findById(itemId)).thenReturn(Optional.ofNullable(item));
		when(cartRepository.save(any(Cart.class))).thenReturn(cart);

		// Act
		ResponseEntity<Cart> response = cartController.addTocart(request);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(cart, response.getBody());
		assertEquals(quantity,
				cart.getItems().stream().filter(i -> i.getId().equals(itemId)).collect(Collectors.toList()).size());

		verify(userRepository, times(1)).findByUsername(username);
		verify(itemRepository, times(1)).findById(itemId);
		verify(cartRepository, times(1)).save(ArgumentMatchers.any(Cart.class));
	}

	@Test
	public void testAddToCartInvalid_01() {
		// Arrange
		String username = "testuser";
		Long itemId = 1L;
		int quantity = 1;

		ModifyCartRequest request = new ModifyCartRequest();
		request.setUsername(username);
		request.setItemId(itemId);
		request.setQuantity(quantity);

		when(userRepository.findByUsername(username)).thenReturn(null);

		// Act
		ResponseEntity<Cart> response = cartController.addTocart(request);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

	}

	@Test
	public void testAddToCartInvalid_02() {
		// Arrange
		String username = "testuser";
		Long itemId = 1L;
		int quantity = 1;

		User user = new User();
		user.setId(1L);
		user.setUsername(username);

		ModifyCartRequest request = new ModifyCartRequest();
		request.setUsername(username);
		request.setItemId(itemId);
		request.setQuantity(quantity);

		when(userRepository.findByUsername(username)).thenReturn(user);
		when(itemRepository.findById(itemId)).thenReturn(Optional.ofNullable(null));

		// Act
		ResponseEntity<Cart> response = cartController.addTocart(request);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

	}

	@Test
	public void testRemoveFromCart() {

		// Arrange
		String username = "testuser";
		Long itemId = 1L;
		int quantity = 1;

		User user = new User();
		user.setId(1L);
		user.setUsername(username);

		Item item = new Item();
		item.setId(itemId);
		item.setName("Test Item");
		item.setDescription("Test Item Description");
		item.setPrice(new BigDecimal("10.00"));

		ModifyCartRequest request = new ModifyCartRequest();
		request.setUsername(username);
		request.setItemId(itemId);
		request.setQuantity(quantity);

		Cart cart = new Cart();
		cart.setId(1L);
		cart.setUser(user);
		List<Item> items = new ArrayList<>();
		items.add(item);
		cart.setItems(items);

		user.setCart(cart);

		when(userRepository.findByUsername(request.getUsername())).thenReturn(user);
		when(itemRepository.findById(request.getItemId())).thenReturn(Optional.of(item));

		ResponseEntity<Cart> response = cartController.removeFromcart(request);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(0, cart.getItems().size());
		verify(cartRepository, times(1)).save(cart);
	}

	@Test
	public void testRemoveFromCartInvalid_01() {

		String username = "testuser";
		User user = new User();
		user.setId(1L);
		user.setUsername(username);
		ModifyCartRequest request = new ModifyCartRequest();

		when(userRepository.findByUsername(request.getUsername())).thenReturn(null);

		ResponseEntity<Cart> response = cartController.removeFromcart(request);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void testRemoveFromCartInvalid_02() {

		// Arrange
		String username = "testuser";

		User user = new User();
		user.setId(1L);
		user.setUsername(username);

		ModifyCartRequest request = new ModifyCartRequest();

		when(userRepository.findByUsername(request.getUsername())).thenReturn(user);
		when(itemRepository.findById(request.getItemId())).thenReturn(Optional.ofNullable(null));

		ResponseEntity<Cart> response = cartController.removeFromcart(request);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

}
