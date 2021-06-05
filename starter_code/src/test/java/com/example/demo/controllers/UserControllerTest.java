package com.example.demo.controllers;

//help obtained from https://knowledge.udacity.com/questions/595874

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;


public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
    @Before

    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", passwordEncoder);
        User user = new User();
        Cart cart = new Cart();
        user.setId(0);
        user.setUsername("demo");
//    user.setPassword("testPassword");
        user.setCart(cart);
        when(userRepository.findByUsername("demo")).thenReturn(user);
        when(userRepository.findById(0L)).thenReturn(java.util.Optional.of(user));
        when(userRepository.findByUsername("someone")).thenReturn(null);
        }
    //@Test
    //public void createUser(){
    //    // TODO: add and check passwords
    //    CreateUserRequest createUserRequest = new CreateUserRequest();
    //    createUserRequest.setUsername("Paul");
    //    createUserRequest.setPassword("testPW");
    //    createUserRequest.setConfirmPassword("testPW");
    //    ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);
    //    assertNotNull(responseEntity);
    //    assertEquals(200, responseEntity.getStatusCodeValue());
    //    User user = responseEntity.getBody();
    //    assertNotNull(user);
    //    assertEquals(0, user.getId());
    //    assertEquals("demo", user.getUsername());
    //    }
    @Test
    public void findUserByUsername(){
        ResponseEntity<User> userResponseEntity = userController.findByUserName("demo");
        assertNotNull(userResponseEntity);
        assertEquals(200, userResponseEntity.getStatusCodeValue());
        User user = userResponseEntity.getBody();
        assertNotNull(user);
        assertEquals("demo", user.getUsername());
        }
    @Test
    public void findUserByUserNameNotFound(){
        ResponseEntity<User> userResponseEntity = userController.findByUserName("medo");
        assertNotNull(userResponseEntity);
        assertEquals(404, userResponseEntity.getStatusCodeValue());
        }
    @Test
    public void findUserById(){
        ResponseEntity<User> userResponseEntity = userController.findById(0L);
        assertNotNull(userResponseEntity);
        assertEquals(200, userResponseEntity.getStatusCodeValue());
        User user = userResponseEntity.getBody();
        assertNotNull(user);
        assertEquals("demo", user.getUsername());
        }
    @Test
    public void findUserByIdNotFound(){
        // TODO this isnt working so lekker
        ResponseEntity<User> userResponseEntity = userController.findById(999L);
        System.out.println(userResponseEntity);
        assertNotNull(userResponseEntity);
        assertEquals(404, userResponseEntity.getStatusCodeValue());
        }
}

/*
public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void SetUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception{
        //This is an example of "Stubbing".
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        //End stubbing example.
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId() );
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void create_user_with_short_password_unhappy_path() throws Exception{
        //In UserController a ResponseEntity.badRequest().build(); will be returned if the
        //password is less that 7 characters long, so check for status 400
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("short");
        r.setConfirmPassword("short");

        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }
}
*/

