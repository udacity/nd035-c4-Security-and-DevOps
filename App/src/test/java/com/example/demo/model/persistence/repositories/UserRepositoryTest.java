package com.example.demo.model.persistence.repositories;

import com.example.demo.model.persistence.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;


    @Test
    @Transactional
    public void findByUsernameTest(){


        // happy path
        User user = new User();

        user.setUsername("Username");
        user.setPassword("password");

        userRepository.save(user);

        User foundUser = userRepository.findByUsername("Username");

        Assert.assertNotNull(foundUser);
        Assert.assertEquals("Username",foundUser.getUsername());
        Assert.assertEquals("password",foundUser.getPassword());
        Assert.assertNotNull(foundUser.getId());


        // negative test
        User unknownUser = userRepository.findByUsername("Unknown");
        Assert.assertNull(unknownUser);


    }

}
