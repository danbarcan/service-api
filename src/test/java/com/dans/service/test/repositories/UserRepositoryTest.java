package com.dans.service.test.repositories;

import com.dans.service.entities.User;
import com.dans.service.repositories.UserRepository;
import org.assertj.core.api.Assertions;
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
                .name("test")
                .username("test")
                .phoneNumber("07test")
                .build();

    @Test
    public void findByUsernameWhenNoUserShouldReturnNull() {
        this.entityManager.persist(this.user);
        Optional<User> user = this.userRepository.findByEmail("mmouse");
        Assertions.assertThat(user.isPresent()).isFalse();
    }

    @Test
    public void findByUsernameShouldReturnUser() {
        this.entityManager.persist(this.user);
        Optional<User> user = this.userRepository.findByEmail(this.user.getEmail());
        Assertions.assertThat(user.isPresent()).isTrue();
        Assertions.assertThat(user.get().equals(this.user)).isTrue();
    }

    @Test
    public void existsByEmailWhenNoUserShouldReturnFalse() {
        this.entityManager.persist(this.user);
        Boolean exists = this.userRepository.existsByEmail("mmouse");
        Assertions.assertThat(exists).isFalse();
    }

    @Test
    public void existsByEmailShouldReturnTrue() {
        this.entityManager.persist(this.user);
        Boolean exists = this.userRepository.existsByEmail(this.user.getEmail());
        Assertions.assertThat(exists).isTrue();
    }

    @Test
    public void existsByUsernameWhenNoUserShouldReturnFalse() {
        this.entityManager.persist(this.user);
        Boolean exists = this.userRepository.existsByUsername("mmouse");
        Assertions.assertThat(exists).isFalse();
    }

    @Test
    public void existsByUsernameShouldReturnTrue() {
        this.entityManager.persist(this.user);
        Boolean exists = this.userRepository.existsByUsername(this.user.getUsername());
        Assertions.assertThat(exists).isTrue();
    }

}
