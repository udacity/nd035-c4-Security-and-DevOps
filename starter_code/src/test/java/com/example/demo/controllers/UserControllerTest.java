package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserControllerTest {

    private UserController userController;

    private final UserRepository userRepo = mock(UserRepository.class);

    private final CartRepository cartRepo = mock(CartRepository.class);

    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepo);
        TestUtils.injectObject(userController, "cartRepository", cartRepo);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUserTest() throws Exception{

        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");

        CreateUserRequest r = new CreateUserRequest();


        r.setUsername("Jeremy");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();

        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("Jeremy", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void findUserByIdTest() throws Exception{
        User mockUser = createMockUser();

        when(userRepo.findById(mockUser.getId())).thenReturn((java.util.Optional.of(mockUser)));

        final ResponseEntity<User> responseEntity = userController.findById(mockUser.getId());
        User user = responseEntity.getBody();

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        assertNotNull(user);
        assertEquals(mockUser.getId(), user.getId());
        assertEquals(mockUser.getUsername(),user.getUsername());

    }

    //Test password length check in createUser method
    @Test
    public void userPasswordLengthTest() throws Exception{
        CreateUserRequest r = new CreateUserRequest();


        r.setUsername("Jeremy");
        r.setPassword("test");
        r.setConfirmPassword("test");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void findUserByUserNameTest () throws Exception{
        User mockUser = createMockUser();

        when(userRepo.findByUsername(mockUser.getUsername())).thenReturn(mockUser);

        final ResponseEntity<User> responseEntity = userController.findByUserName(mockUser.getUsername());
        User user = responseEntity.getBody();

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        assertNotNull(user);
        assertEquals(mockUser.getId(), user.getId());
        assertEquals(mockUser.getUsername(), user.getUsername());
    }

    //Helper methods

    private User createMockUser (){
        User mockUser = mock(User.class);
        mockUser.setId(1L);
        mockUser.setUsername("mockUserJeremy");
        return mockUser;
    }

}
