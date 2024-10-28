package uz.jumanazar.ecommerceapp.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.jumanazar.ecommerceapp.model.persistence.User;
import uz.jumanazar.ecommerceapp.model.persistence.repositories.UserRepository;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	@Autowired
	private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("[loadUserByUsername] Authentication for username {}", username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("[loadUserByUsername] User not found with username {}", username);
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.emptyList());
    }
}
