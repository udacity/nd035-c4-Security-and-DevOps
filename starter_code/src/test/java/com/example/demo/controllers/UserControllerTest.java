package com.example.demo.controllers;

import com.example.demo.utils.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;


import javax.transaction.Transactional;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserControllerTest {
    private UserController userController;

    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        userController = new UserController();
        TestUtils.injectObjects(userController,"userRepository", userRepository);
        TestUtils.injectObjects(userController,"cartRepository", cartRepository);
        TestUtils.injectObjects(userController,"bCryptPasswordEncoder", bCryptPasswordEncoder);

    }
    @Test
    public  void create_user_happy_path() {
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");

        final ResponseEntity<User> responseEntity = userController.createUser(request);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        User u = responseEntity.getBody();
        assert u != null;
        assertNotNull(u);

        assertEquals(0,u.getId());
        assertEquals("test",u.getUsername());
        assertEquals("thisIsHashed",u.getPassword());
    }

    @Test
    public void find_by_id(){
        User mockUser = new User();
        mockUser.setUsername("test");
        mockUser.setId(1);
        Mockito.<Optional<Object>>when(Optional.of(userRepository.findById(1L))).thenReturn(Optional.of(mockUser));
        User u = userController.findById(1L).getBody();
        assert u != null;
        assertNotNull(u);
       assertEquals(1,u.getId());
       assertNotEquals(0,u.getId());
    }
    @Test
    public void find_by_userName(){
        User mockUser = new User();
        mockUser.setUsername("test");
        mockUser.setId(1);
        when(userRepository.findByUsername("test")).thenReturn(mockUser);
        User u = userController.findByUserName("test").getBody();
        assert u != null;
        assertNotNull(u);
        assertEquals(1,u.getId());
        assertNotNull("test",u.getUsername());
    }


}
