package com.example.demo.controller;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTests {

    @Test
    public void testFindByIdHappyPath() {
        // Arrange
        val userRepositoryMock = mock(UserRepository.class);
        val userId = 1L;
        val testUser = new User();
        testUser.setId(userId);
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(testUser));

        val userController = new UserController(userRepositoryMock, null, null);

        // Act
        val userResponseEntity = userController.findById(userId);

        //Assert
        assertEquals(HttpStatus.OK, userResponseEntity.getStatusCode());
        assertEquals(testUser, userResponseEntity.getBody());

        verify(userRepositoryMock).findById(userId);
    }

    @Test
    public void testFindByIdUnhappyPathUserNotFound() {
        // Arrange
        val userRepositoryMock = mock(UserRepository.class);
        val userId = 1L;
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

        val userController = new UserController(userRepositoryMock, null, null);

        // Act
        val userResponseEntity = userController.findById(userId);

        //Assert
        assertEquals(HttpStatus.NOT_FOUND, userResponseEntity.getStatusCode());
        assertNull(userResponseEntity.getBody());

        verify(userRepositoryMock).findById(userId);
    }

    @Test
    public void testFindByUserNameHappyPath() {
        // Arrange
        val userRepositoryMock = mock(UserRepository.class);
        val username = "testUser";
        val testUser = new User();
        testUser.setUsername(username);
        when(userRepositoryMock.findByUsername(username)).thenReturn(testUser);

        val userController = new UserController(userRepositoryMock, null, null);

        // Act
        val userResponseEntity = userController.findByUserName(username);

        //Assert
        assertEquals(HttpStatus.OK, userResponseEntity.getStatusCode());
        assertEquals(testUser, userResponseEntity.getBody());

        verify(userRepositoryMock).findByUsername(username);
    }

    @Test
    public void testFindByUserNameUnhappyPathUserNotFound() {
        // Arrange
        val userRepositoryMock = mock(UserRepository.class);
        val username = "testUser";
        when(userRepositoryMock.findByUsername(username)).thenReturn(null);

        val userController = new UserController(userRepositoryMock, null, null);

        // Act
        val userResponseEntity = userController.findByUserName(username);

        //Assert
        assertEquals(HttpStatus.NOT_FOUND, userResponseEntity.getStatusCode());
        assertNull(userResponseEntity.getBody());

        verify(userRepositoryMock).findByUsername(username);
    }

    @Test
    public void testCreateUserHappyPath() {
        // Arrange
        val username = "testUser";
        val password = "testPassword";
        val createUserRequest = new CreateUserRequest(username, password, password);

        val userRepositoryMock = mock(UserRepository.class);
        val cartRepositoryMock = mock(CartRepository.class);

        val userController = new UserController(userRepositoryMock, cartRepositoryMock, new BCryptPasswordEncoder());

        // Act
        val userResponseEntity = userController.createUser(createUserRequest);

        //Assert
        assertEquals(HttpStatus.OK, userResponseEntity.getStatusCode());

        val user = userResponseEntity.getBody();
        assertNotNull(user);
        assertEquals(username, user.getUsername());

        verify(cartRepositoryMock).save(any());
        verify(userRepositoryMock).save(any());
    }

    @Test
    public void testCreateUserUnhappyPathPasswordTooShort() {
        // Arrange
        val username = "testUser";
        val password = "short";
        val createUserRequest = new CreateUserRequest(username, password, password);

        val userRepositoryMock = mock(UserRepository.class);
        val cartRepositoryMock = mock(CartRepository.class);
        val bCryptPasswordEncoderMock = mock(BCryptPasswordEncoder.class);

        val userController = new UserController(userRepositoryMock, cartRepositoryMock, bCryptPasswordEncoderMock);

        // Act
        val userResponseEntity = userController.createUser(createUserRequest);

        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, userResponseEntity.getStatusCode());
        assertNull(userResponseEntity.getBody());

        verify(cartRepositoryMock, never()).save(any());
        verify(bCryptPasswordEncoderMock, never()).encode(any());
        verify(userRepositoryMock, never()).save(any());
    }

    @Test
    public void testCreateUserUnhappyPathConfirmPasswordDoesNotMatch() {
        // Arrange
        val username = "testUser";
        val password = "testPassword";
        val confirmPassword = "NotTestPassword";
        val createUserRequest = new CreateUserRequest(username, password, confirmPassword);

        val userRepositoryMock = mock(UserRepository.class);
        val cartRepositoryMock = mock(CartRepository.class);
        val bCryptPasswordEncoderMock = mock(BCryptPasswordEncoder.class);

        val userController = new UserController(userRepositoryMock, cartRepositoryMock, bCryptPasswordEncoderMock);

        // Act
        val userResponseEntity = userController.createUser(createUserRequest);

        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, userResponseEntity.getStatusCode());
        assertNull(userResponseEntity.getBody());

        verify(cartRepositoryMock, never()).save(any());
        verify(bCryptPasswordEncoderMock, never()).encode(any());
        verify(userRepositoryMock, never()).save(any());
    }
}
