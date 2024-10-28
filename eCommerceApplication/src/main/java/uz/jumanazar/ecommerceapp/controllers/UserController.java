package uz.jumanazar.ecommerceapp.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uz.jumanazar.ecommerceapp.model.persistence.Cart;
import uz.jumanazar.ecommerceapp.model.persistence.User;
import uz.jumanazar.ecommerceapp.model.persistence.repositories.CartRepository;
import uz.jumanazar.ecommerceapp.model.persistence.repositories.UserRepository;
import uz.jumanazar.ecommerceapp.model.requests.CreateUserRequest;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger log = LogManager.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        log.info("[findById] New request for searching a user by ID {}", id);
        return ResponseEntity.of(userRepository.findById(id));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        log.info("[findByUserName] New request for searching a user by userName {}", username);
        User user = userRepository.findByUsername(username);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest createUserRequest) {
//        log.info("[createUser] New request for user creation with request body {}", createUserRequest);
        try {
            User user = new User();
            user.setUsername(createUserRequest.getUsername());
            Cart cart = new Cart();
            cartRepository.save(cart);
            user.setCart(cart);
            if (createUserRequest.getPassword().length() < 7 || !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("statusCode", 400);
                response.put("errorMessage", "Bad password was provided. Please, check the password requirements (at least 8 chars) " +
                        "and make sure if the password and confirmPasswords are the same.");
                log.error("[createUser_failure] Bad password was provided for username {}", createUserRequest.getUsername());
                return ResponseEntity
                        .status(400)
                        .body(response);
            }
            user.setPassword(encoder.encode(createUserRequest.getPassword()));
            userRepository.save(user);
            log.info("[createUser_success] User with username {} was successfully created", user.getUsername());
            return ResponseEntity.ok(user);
        }catch (Exception e){
            log.error("[createUser_failure] User creation failed with error {}", e.getMessage());
        }
        return ResponseEntity.status(500).build();
    }
}
