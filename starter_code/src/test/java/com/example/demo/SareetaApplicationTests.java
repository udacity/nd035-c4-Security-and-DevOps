package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.OrderController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

/* @RunWith(SpringRunner.class)
@SpringBootTest


 */
public class SareetaApplicationTests {

	private UserController userController;

	private CartController cartController;

	private ItemController itemController;

	private OrderController orderController;

	private final UserRepository userRepository = mock(UserRepository.class);

	private final CartRepository cartRepository = mock(CartRepository.class);

	private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

	private final ItemRepository itemRepository = mock(ItemRepository.class);

	private final OrderRepository orderRepository = mock(OrderRepository.class);


	@Before
	public void setup() {

		userController = new UserController();
		cartController = new CartController();
		itemController = new ItemController();
		orderController = new OrderController();
		TestUtils.injectObjects(userController, "userRepository", userRepository);
		TestUtils.injectObjects(userController, "cartRepository", cartRepository);
		TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
		TestUtils.injectObjects(cartController, "userRepository", userRepository);
		TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
		TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
		TestUtils.injectObjects(orderController, "userRepository", userRepository);
		TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
	}

	@Test
	public void testInvalidPassword() {

		User testUser = new User();

		testUser.setUsername("TestUser0");
		testUser.setPassword("1234");

		assertEquals("", 400,
				userController.createUser(sampleCreateUserRequest(testUser)).getStatusCodeValue());


	}


	@Test
	public void testCreateUser() {

		User expectedUser = new User();

		expectedUser.setUsername("TestUser1");
		expectedUser.setPassword("12345678");

		User receivedUser = userController.createUser(sampleCreateUserRequest(expectedUser)).getBody();

		assertEquals("", expectedUser.getUsername(), receivedUser.getUsername());


	}

	@Test
	public void testFindUserById() {

		User newUser = new User();

		newUser.setUsername("TestUser2");
		newUser.setPassword("12345678");

		when(userRepository.findById(newUser.getId())).
				thenReturn((java.util.Optional.of(newUser)));

		User receivedUser = userController.createUser(sampleCreateUserRequest(newUser)).getBody();

		Long receivedUserId = receivedUser.getId();

		assertEquals("", receivedUser.getUsername(),
				userController.findById(receivedUserId).getBody().getUsername());

	}


	@Test
	public void testFindUserByUsername() {

		User newUser = new User();

		newUser.setUsername("TestUser3");
		newUser.setPassword("12345678");

		when(userRepository.findByUsername(newUser.getUsername())).
				thenReturn((newUser));


		User receivedUser = userController.createUser(sampleCreateUserRequest(newUser)).getBody();

		String receivedUserUsername = receivedUser.getUsername();

		assertEquals("", receivedUser.getId(),
				userController.findByUserName(receivedUserUsername).getBody().getId());


	}

	@Test
	public void testAddToCart() {

		User newUser = new User();

		newUser.setUsername("TestUser4");
		newUser.setPassword("12345678");

		Item item1 = new Item();
		Item item2 = new Item();

		item1.setId(4L);
		item1.setName("Round Widget");
		item1.setPrice(BigDecimal.valueOf(2.99));
		item1.setDescription("A widget that is round");

		item2.setId(5L);
		item2.setName("Square Widget");
		item2.setPrice(BigDecimal.valueOf(1.99));
		item2.setDescription("A widget that is square");

		Cart newCart = new Cart();

		newCart.setId(1L);
		newCart.addItem(item1);
		newCart.setUser(newUser);

		newUser.setCart(newCart);

		when(userRepository.findByUsername(newUser.getUsername())).thenReturn(newUser);
		when(itemRepository.findById(item1.getId())).thenReturn(Optional.of(item1));
		when(itemRepository.findById(item2.getId())).thenReturn(Optional.of(item2));


		cartController.addTocart(
				sampleModifyCartRequest(newUser, item1, 1)).getBody();
		Cart receivedCart = cartController.addTocart(
				sampleModifyCartRequest(newUser, item2, 1)).getBody();

		List<String> items = receivedCart.getItems().stream().
				map(i -> {return i.getName();}).collect(Collectors.toList());


		assertTrue(items.contains("Round Widget"));
		assertTrue(items.contains("Square Widget"));



	}

	@Test
	public void testRemoveFromCart() {

		User newUser = new User();

		newUser.setUsername("TestUser5");
		newUser.setPassword("12345678");

		Item item1 = new Item();
		Item item2 = new Item();

		item1.setId(4L);
		item1.setName("Round Widget");
		item1.setPrice(BigDecimal.valueOf(2.99));
		item1.setDescription("A widget that is round");

		item2.setId(5L);
		item2.setName("Square Widget");
		item2.setPrice(BigDecimal.valueOf(1.99));
		item2.setDescription("A widget that is square");

		Cart newCart = new Cart();

		newCart.setId(1L);
		newCart.addItem(item1);
		newCart.setUser(newUser);

		newUser.setCart(newCart);

		when(userRepository.findByUsername(newUser.getUsername())).thenReturn(newUser);
		when(itemRepository.findById(item1.getId())).thenReturn(Optional.of(item1));
		when(itemRepository.findById(item2.getId())).thenReturn(Optional.of(item2));


		cartController.addTocart(
				sampleModifyCartRequest(newUser, item1, 1)).getBody();
		Cart receivedCart = cartController.addTocart(
				sampleModifyCartRequest(newUser, item2, 1)).getBody();

		List<String> items = receivedCart.getItems().stream().
				map(i -> {return i.getName();}).collect(Collectors.toList());


		assertTrue(items.contains("Round Widget"));
		assertTrue(items.contains("Square Widget"));

		receivedCart = cartController.removeFromcart(sampleModifyCartRequest(newUser,
				item2, 1)).getBody();

		items = receivedCart.getItems().stream().
				map(i -> {return i.getName();}).collect(Collectors.toList());

		assertFalse(items.contains("Square Widget"));

	}

	@Test
	public void testSubmitOrder() {

		User newUser = new User();

		newUser.setUsername("TestUser6");
		newUser.setPassword("12345678");

		Item item1 = new Item();
		Item item2 = new Item();

		item1.setId(4L);
		item1.setName("Round Widget");
		item1.setPrice(BigDecimal.valueOf(2.99));
		item1.setDescription("A widget that is round");

		item2.setId(5L);
		item2.setName("Square Widget");
		item2.setPrice(BigDecimal.valueOf(1.99));
		item2.setDescription("A widget that is square");

		Cart newCart = new Cart();

		newCart.setId(1L);
		newCart.addItem(item1);
		newCart.setUser(newUser);

		newUser.setCart(newCart);

		when(userRepository.findByUsername(newUser.getUsername())).thenReturn(newUser);
		when(itemRepository.findById(item1.getId())).thenReturn(Optional.of(item1));
		when(itemRepository.findById(item2.getId())).thenReturn(Optional.of(item2));


		cartController.addTocart(
				sampleModifyCartRequest(newUser, item1, 1)).getBody();
		Cart receivedCart = cartController.addTocart(
				sampleModifyCartRequest(newUser, item2, 1)).getBody();

		List<String> items = receivedCart.getItems().stream().
				map(i -> {return i.getName();}).collect(Collectors.toList());


		assertTrue(items.contains("Round Widget"));
		assertTrue(items.contains("Square Widget"));


		UserOrder userOrder = orderController.submit(newUser.getUsername()).getBody();

		List<String> orderItems = userOrder.getItems().stream().
				map(i -> {return i.getName();}).collect(Collectors.toList());

		items.forEach((i) -> assertTrue(orderItems.contains(i)));
	}

	/*
	@Test
	public void testGetOrdersForUser() {

		User newUser = new User();

		newUser.setUsername("TestUser6");
		newUser.setPassword("12345678");

		User sampleUser = userController.createUser(sampleCreateUserRequest(newUser)).getBody();

		Item item1 = itemController.getItemsByName("Round Widget").getBody().get(0);
		Item item2 = itemController.getItemsByName("Square Widget").getBody().get(0);

		Cart newCart = new Cart();

		newCart.setUser(sampleUser);

		Cart receivedCart = new Cart();

		cartController.addTocart(sampleModifyCartRequest(newUser, item1, 1));
		receivedCart = cartController.addTocart(sampleModifyCartRequest(newUser, item2, 1)).getBody();

		List<String> items = receivedCart.getItems().stream().
				map(i -> {return i.getName();}).collect(Collectors.toList());


		assertTrue(items.contains("Round Widget"));
		assertTrue(items.contains("Square Widget"));

		orderController.submit(sampleUser.getUsername());

		UserOrder userOrder = orderController.getOrdersForUser(sampleUser.getUsername()).getBody().get(0);

		List<String> orderItems = userOrder.getItems().stream().
				map(i -> {return i.getName();}).collect(Collectors.toList());

		items.forEach((i) -> assertTrue(orderItems.contains(i)));


	}

*/


	@Test
	public void contextLoads() {
	}


	private CreateUserRequest sampleCreateUserRequest (User user) {

		CreateUserRequest createUserRequest = new CreateUserRequest();

		createUserRequest.setUsername(user.getUsername());

		createUserRequest.setPassword(user.getPassword());

		createUserRequest.setConfirmPassword(user.getPassword());

		return createUserRequest;

	}

	private ModifyCartRequest sampleModifyCartRequest(User user, Item item, int quantity) {

		ModifyCartRequest modifyCartRequest = new ModifyCartRequest();

		modifyCartRequest.setUsername(user.getUsername());
		modifyCartRequest.setItemId(item.getId());
		modifyCartRequest.setQuantity(quantity);

		return modifyCartRequest;


	}


}
