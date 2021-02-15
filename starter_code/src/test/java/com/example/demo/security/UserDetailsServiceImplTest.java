package com.example.demo.security;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class UserDetailsServiceImplTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final User USER = new User(USERNAME, PASSWORD);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(userRepository);

    @Test
    public void loadUserByUsernameReturnsUserForValidInput() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(USER);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(USERNAME);

        assertEquals(userDetails.getUsername(), USERNAME);
        assertEquals(userDetails.getPassword(), PASSWORD);
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameThrowsForUnknownUsername() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(null);

        userDetailsService.loadUserByUsername(USERNAME);
    }

}
