package com.dans.service.test.repositories;

import com.dans.service.entities.User;
import com.dans.service.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByUsernameShouldReturnUser() {
        this.entityManager.persist(User.builder().email("test@test.com")
                .password("password")
                .firstName("test")
                .lastName("test")
                .phoneNumber("07test")
                .build());
        Optional<User> user = this.userRepository.findByEmail("test@test.com");
        Assertions.assertThat(user.isPresent());
        Assertions.assertThat(user.get().getEmail().equals("test@test.com"));
        Assertions.assertThat(user.get().getFirstName().equals("test"));
        Assertions.assertThat(user.get().getLastName().equals("test"));
        Assertions.assertThat(user.get().getPhoneNumber().equals("07test"));
    }

    @Test
    public void findByUsernameWhenNoUserShouldReturnNull() {
        this.entityManager.persist(User.builder().email("test@test.com")
                .password("password")
                .firstName("test")
                .lastName("test")
                .phoneNumber("07test")
                .build());
        Optional<User> user = this.userRepository.findByEmail("mmouse");
        Assertions.assertThat(!user.isPresent());
    }

}
