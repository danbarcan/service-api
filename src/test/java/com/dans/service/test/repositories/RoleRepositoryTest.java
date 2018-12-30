package com.dans.service.test.repositories;

import com.dans.service.entities.Role;
import com.dans.service.entities.RoleName;
import com.dans.service.repositories.RoleRepository;
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
public class RoleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    private Role role = Role.builder().name(RoleName.ROLE_USER).build();


    @Test
    public void existsByEmailWhenNoUserShouldReturnFalse() {
        this.entityManager.persist(this.role);
        Optional<Role> role = this.roleRepository.findByName(RoleName.ROLE_ADMIN);
        Assertions.assertThat(role.isPresent()).isFalse();
    }

    @Test
    public void existsByEmailShouldReturnTrue() {
        this.entityManager.persist(this.role);
        Optional<Role> role = this.roleRepository.findByName(this.role.getName());
        Assertions.assertThat(role.isPresent()).isTrue();
        Assertions.assertThat(this.role.equals(role.get())).isTrue();
    }
}
