package uz.jumanazar.ecommerceapp.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import uz.jumanazar.ecommerceapp.TestUtils;
import uz.jumanazar.ecommerceapp.model.persistence.User;
import uz.jumanazar.ecommerceapp.model.persistence.repositories.CartRepository;
import uz.jumanazar.ecommerceapp.model.persistence.repositories.UserRepository;
import uz.jumanazar.ecommerceapp.model.requests.CreateUserRequest;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private final UserRepository userRepo = mock(UserRepository.class);
    private final CartRepository cartRepo = mock(CartRepository.class);
    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "encoder", encoder);
    }

    @Test
    public void create_user() throws Exception{
        when(encoder.encode("testtest")).thenReturn("thisIsHashed");
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("test");
        req.setPassword("testtest");
        req.setConfirmPassword("testtest");

        final ResponseEntity<?> response = userController.createUser(req);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        assertTrue((response.getBody() instanceof User));
        User user = (User) response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void user_create_fails() {
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("test");
        req.setPassword("test12");
        req.setConfirmPassword("test12");
        final ResponseEntity<?> response = userController.createUser(req);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.getBody() instanceof User);
    }

    @Test
    public void findById() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("test1234");

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        final ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User userReturned = response.getBody();
        assertNotNull(userReturned);
        assertEquals(user.getId(), userReturned.getId());
        assertEquals(user.getUsername(), userReturned.getUsername());
    }

    @Test
    public void findByUserName() {
        User user = new User();
        user.setUsername("test");
        String userName = "test";
        when(userRepo.findByUsername(userName)).thenReturn(user);

        ResponseEntity<User> response = userController.findByUserName(userName);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User userReturned = response.getBody();
        assertNotNull(userReturned);
        assertEquals(userName, userReturned.getUsername());
    }
}
