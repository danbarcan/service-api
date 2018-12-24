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

    private User user = User.builder().email("test@test.com")
                .password("password")
                .firstName("test")
                .lastName("test")
                .phoneNumber("07test")
                .build();

    @Test
    public void findByUsernameShouldReturnUser() {
        this.entityManager.persist(this.user);
        Optional<User> user = this.userRepository.findByEmail("test@test.com");
        Assertions.assertThat(user.isPresent()).isTrue();
        Assertions.assertThat(user.get().getEmail().equals(this.user.getEmail())).isTrue();
        Assertions.assertThat(user.get().getFirstName().equals(this.user.getFirstName())).isTrue();
        Assertions.assertThat(user.get().getLastName().equals(this.user.getLastName())).isTrue();
        Assertions.assertThat(user.get().getPhoneNumber().equals(this.user.getPhoneNumber())).isTrue();
        Assertions.assertThat(user.get().equals(this.user)).isTrue();
    }

    @Test
    public void findByUsernameWhenNoUserShouldReturnNull() {
        this.entityManager.persist(this.user);
        Optional<User> user = this.userRepository.findByEmail("mmouse");
        Assertions.assertThat(user.isPresent()).isFalse();
    }

}
