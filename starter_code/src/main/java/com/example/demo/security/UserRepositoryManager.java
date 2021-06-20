package com.example.demo.security;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserRepositoryManager implements UserDetailsService {

    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    private final PasswordEncoder passwordEncoder;

    public UserRepositoryManager(UserRepository userRepository,
                                 CartRepository cartRepository,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public Optional<User> createUser(String username, String password) {
        if (this.userRepository.findByUsername(username).isPresent()) return Optional.empty();

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        Cart cart = new Cart();
        cartRepository.save(cart);
        user.setCart(cart);
        return Optional.of(userRepository.save(user));
    }
}
