package com.dans.service.test.services;

import com.dans.service.entities.Role;
import com.dans.service.entities.RoleName;
import com.dans.service.entities.ServiceDetails;
import com.dans.service.entities.User;
import com.dans.service.payloads.ApiResponse;
import com.dans.service.payloads.JwtAuthenticationResponse;
import com.dans.service.payloads.LoginPayload;
import com.dans.service.payloads.SignUpPayload;
import com.dans.service.repositories.RoleRepository;
import com.dans.service.repositories.UserRepository;
import com.dans.service.security.JwtTokenProvider;
import com.dans.service.services.AuthService;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthServiceTest {

    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private RoleRepository roleRepository;

    private ServiceDetails serviceDetails = ServiceDetails.builder()
            .address("test")
            .cui(111L)
            .name("ServiceDetails")
            .build();

    private User service = User.builder().email("service@test.com")
            .password("password")
            .name("test")
            .username("service")
            .phoneNumber("07test")
            .serviceDetails(serviceDetails)
            .build();

    private User user = User.builder().email("test@test.com")
            .password("password")
            .name("test")
            .username("test")
            .phoneNumber("07test")
            .build();

    private SignUpPayload signUpPayloadUser = new SignUpPayload("test", "test", "test", "test", "test", "", "", "0");

    private SignUpPayload signUpPayloadService = new SignUpPayload("test", "test", "test", "test", "test", "test", "test", "0");

    private LoginPayload loginPayload = new LoginPayload("test@test.com", "password");

    private Role roleUser = Role.builder().name(RoleName.ROLE_USER).build();
    private Role roleService = Role.builder().name(RoleName.ROLE_SERVICE).build();

    @Before
    public void setUp() {
        authService = new AuthService(authenticationManager, tokenProvider, passwordEncoder, userRepository, roleRepository);
    }

    @Test
    public void signUpExistingUserMailResponseBadRequest() {

        BDDMockito.given(this.userRepository.existsByEmail(BDDMockito.any(String.class))).willReturn(true);
        Assert.assertThat(authService.registerUser(signUpPayloadUser),
                Is.is(new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                        HttpStatus.BAD_REQUEST)));
    }

    @Test
    public void signUpExistingUsernameResponseBadRequest() {

        BDDMockito.given(this.userRepository.existsByEmail(BDDMockito.any(String.class))).willReturn(false);
        BDDMockito.given(this.userRepository.existsByUsername(BDDMockito.any(String.class))).willReturn(true);
        Assert.assertThat(authService.registerUser(signUpPayloadUser),
                Is.is(new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                        HttpStatus.BAD_REQUEST)));
    }

    @Test
    @WithMockUser(roles="USER")
    public void signUpUserResponseSuccessful() {

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{usernameOrEmail}")
                .buildAndExpand(user.getUsername()).toUri();

        BDDMockito.given(this.userRepository.existsByEmail(BDDMockito.any(String.class))).willReturn(false);
        BDDMockito.given(this.userRepository.save(BDDMockito.any(User.class))).willReturn(this.user);
        BDDMockito.given(this.roleRepository.findByName(RoleName.ROLE_USER)).willReturn(Optional.ofNullable(this.roleUser));
        Assert.assertThat(authService.registerUser(signUpPayloadUser),
                Is.is(ResponseEntity.created(location)
                        .body(new ApiResponse(true, "User registered successfully"))));
    }

    @Test
    public void signUpServiceResponseSuccessful() {

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{usernameOrEmail}")
                .buildAndExpand(service.getUsername()).toUri();

        BDDMockito.given(this.userRepository.existsByEmail(BDDMockito.any(String.class))).willReturn(false);
        BDDMockito.given(this.userRepository.save(BDDMockito.any(User.class))).willReturn(this.service);
        BDDMockito.given(this.roleRepository.findByName(RoleName.ROLE_SERVICE)).willReturn(Optional.ofNullable(this.roleService));
        Assert.assertThat(authService.registerUser(signUpPayloadService),
                Is.is(ResponseEntity.created(location)
                        .body(new ApiResponse(true, "User registered successfully"))));
    }

    @Test
    public void signInUserResponseNull() {
        Assert.assertThat(authService.authenticateUser(loginPayload),
                Is.is(ResponseEntity.ok(new JwtAuthenticationResponse(null))));
    }
}
