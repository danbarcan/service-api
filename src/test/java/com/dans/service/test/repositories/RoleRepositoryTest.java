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
    private RoleRepository roleRepository;

    private Role roleUser = Role.builder().name(RoleName.ROLE_USER).build();
    private Role roleService = Role.builder().name(RoleName.ROLE_SERVICE).build();
    private Role roleAdmin = Role.builder().name(RoleName.ROLE_ADMIN).build();

    @Test
    public void findByNameShouldReturnNull() {
        Optional<Role> role = this.roleRepository.findByName(RoleName.ROLE_TEST);
        Assertions.assertThat(role.isPresent()).isFalse();
    }

    @Test
    public void existsByEmailShouldReturnTrue() {
        Optional<Role> role = this.roleRepository.findByName(RoleName.ROLE_USER);
        Assertions.assertThat(role.isPresent()).isTrue();
        Assertions.assertThat(this.roleUser.getName().equals(role.get().getName())).isTrue();

        role = this.roleRepository.findByName(RoleName.ROLE_ADMIN);
        Assertions.assertThat(role.isPresent()).isTrue();
        Assertions.assertThat(this.roleAdmin.getName().equals(role.get().getName())).isTrue();

        role = this.roleRepository.findByName(RoleName.ROLE_SERVICE);
        Assertions.assertThat(role.isPresent()).isTrue();
        Assertions.assertThat(this.roleService.getName().equals(role.get().getName())).isTrue();
    }
}
