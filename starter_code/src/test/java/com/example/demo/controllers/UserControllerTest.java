package com.example.demo.controllers;


import com.example.demo.Helper;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
    private final CartRepository cartRepository = mock(CartRepository.class);

    @Before
    public void setup(){
        userController = new UserController();
        TestUtils.injectObjects(userController,"userRepository",userRepository);
        TestUtils.injectObjects(userController,"bCryptPasswordEncoder",bCryptPasswordEncoder);
        TestUtils.injectObjects(userController,"cartRepository", cartRepository);
    }

    @Test
    public void create_user_happy_path_test(){

        CreateUserRequest createUserRequest = Helper.createUserRequest();

        when(bCryptPasswordEncoder.encode("netchas1234")).thenReturn("haschedPassord");

        ResponseEntity<User> userResponse = userController.createUser(createUserRequest);

        Assert.assertNotNull(userResponse);
        Assert.assertEquals(HttpStatus.OK,userResponse.getStatusCode());

        User userReponseBody = userResponse.getBody();

        Assert.assertNotNull(userReponseBody);
        Assert.assertEquals(createUserRequest.getUsername(), userReponseBody.getUsername());
        Assert.assertEquals("haschedPassord", userReponseBody.getPassword());

    }

    @Test
    public void find_user_by_id_happy_path_test(){

        CreateUserRequest createUserRequest = Helper.createUserRequest();

        when(bCryptPasswordEncoder.encode("netchas1234")).thenReturn("haschedPassord");

        ResponseEntity<User> userResponse = userController.createUser(createUserRequest);

        Assert.assertNotNull(userResponse);
        Assert.assertEquals(HttpStatus.OK,userResponse.getStatusCode());

        User userReponseBody = userResponse.getBody();

        Assert.assertNotNull(userReponseBody);
        Assert.assertEquals(0,userReponseBody.getId());
        Assert.assertEquals(createUserRequest.getUsername(), userReponseBody.getUsername());
        Assert.assertEquals("haschedPassord", userReponseBody.getPassword());

        when(userRepository.findById(userReponseBody.getId())).thenReturn(Optional.of(userReponseBody));

        ResponseEntity<User> userResponseEntity = userController.findById(userReponseBody.getId());

        Assert.assertNotNull(userResponseEntity);
        Assert.assertEquals(200, userResponseEntity.getStatusCodeValue());


    }

    @Test
    public void find_user_by_id_not_found(){
        ResponseEntity<User> userResponseEntity = userController.findById(0L);
        Assert.assertNotNull(userResponseEntity);
        Assert.assertEquals(HttpStatus.NOT_FOUND, userResponseEntity.getStatusCode());
    }


    @Test
    public void find_by_username_happy_path_test(){

        CreateUserRequest createUserRequest = Helper.createUserRequest();

        when(bCryptPasswordEncoder.encode("netchas1234")).thenReturn("haschedPassord");

        ResponseEntity<User> userResponse = userController.createUser(createUserRequest);

        Assert.assertNotNull(userResponse);
        Assert.assertEquals(HttpStatus.OK,userResponse.getStatusCode());

        User userResponseBody = userResponse.getBody();

        Assert.assertNotNull(userResponseBody);
        Assert.assertEquals(0,userResponseBody.getId());
        Assert.assertEquals(createUserRequest.getUsername(), userResponseBody.getUsername());
        Assert.assertEquals("haschedPassord", userResponseBody.getPassword());

        when(userRepository.findByUsername(userResponseBody.getUsername())).thenReturn(userResponseBody);

        ResponseEntity<User> userResponseEntity = userController.findByUserName(userResponseBody.getUsername());

        Assert.assertNotNull(userResponseEntity);

        Assert.assertEquals(HttpStatus.OK,userResponse.getStatusCode());
    }

    @Test
    public void find_by_username_not_found_test(){
        ResponseEntity<User> userResponseEntity = userController.findByUserName("Test");
        Assert.assertNotNull(userResponseEntity);
        Assert.assertEquals(HttpStatus.NOT_FOUND, userResponseEntity.getStatusCode());
    }


}
