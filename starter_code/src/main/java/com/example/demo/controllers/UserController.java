package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.security.UserRepositoryManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private final UserRepositoryManager userRepositoryManager;

    public UserController(UserRepository userRepository, UserRepositoryManager userRepositoryManager) {
        this.userRepository = userRepository;
        this.userRepositoryManager = userRepositoryManager;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.of(userRepository.findById(id));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        String password = createUserRequest.getPassword();

        if (password.length() < 7 ||
            !password.equals(createUserRequest.getConfirmPassword())) {
            log.error("Either length is less than 7 or pass and conf pass do not match. Unable to create {}", createUserRequest.getUsername());
            return ResponseEntity.badRequest().build();
        }

        Optional<User> userOptional = this.userRepositoryManager.createUser(createUserRequest.getUsername(), password);

        if (userOptional.isPresent()) {
            log.info("User {} created successfully", createUserRequest.getUsername());
            return ResponseEntity.ok(userOptional.get());
        } else {
            log.error("User {} already exists", createUserRequest.getUsername());
            return ResponseEntity.badRequest().build();
        }
    }
}
