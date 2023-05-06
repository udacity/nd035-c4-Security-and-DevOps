package com.example.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTests {

	@InjectMocks
	private UserController userController;

	@Mock
	private UserRepository userRepository;

	@Mock
	private CartRepository cartRepository;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private CreateUserRequest createUserRequest;

	private User user;

	@Before
	public void setUp() throws Exception {
		createUserRequest = new CreateUserRequest();
		createUserRequest.setUsername("udacity");
		createUserRequest.setPassword("abcd1234");
		createUserRequest.setConfirmPassword("abcd1234");

		user = new User();
		user.setUsername("udacity");
		user.setPassword("abcd1234");

	}

	@Test
	public void testCreateUserWithValidRequest() {

		when(bCryptPasswordEncoder.encode(createUserRequest.getPassword())).thenReturn("encodedPassword");

		ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(createUserRequest.getUsername(), responseEntity.getBody().getUsername());
	}

	@Test
	public void testCreateUserWithInvalidRequest_01() {
		createUserRequest.setPassword("pass");

		ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

	@Test
	public void testCreateUserWithInvalidRequest_02() {
		createUserRequest.setPassword("pass1234");

		ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

	@Test
	public void testFindByIdWithValidRequest() {
		Optional<User> optional = Optional.ofNullable(user);

		when(userRepository.findById((long) 1)).thenReturn(optional);

		ResponseEntity<User> responseEntity = userController.findById((long) 1);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(user, responseEntity.getBody());
	}

	@Test
	public void testFindByUsernameWithValidRequest() {
		when(userRepository.findByUsername(anyString())).thenReturn(user);

		ResponseEntity<User> responseEntity = userController.findByUserName("user1");

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(user, responseEntity.getBody());
	}

	@Test
	public void testFindByUserNameWithInvalidUsername() {
		when(userRepository.findByUsername(anyString())).thenReturn(null);

		ResponseEntity<User> responseEntity = userController.findByUserName("user2");

		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
}
