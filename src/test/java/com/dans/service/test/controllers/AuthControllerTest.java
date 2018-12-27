package com.dans.service.test.controllers;

import com.dans.service.controllers.AuthController;
import com.dans.service.entities.User;
import com.dans.service.payloads.ApiResponse;
import com.dans.service.payloads.SignUpPayload;
import com.dans.service.repositories.UserRepository;
import com.dans.service.security.JwtTokenProvider;
import io.swagger.annotations.Api;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    private AuthController authController;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    private User user = User.builder().email("test@test.com")
            .password("password")
            .firstName("test")
            .lastName("test")
            .phoneNumber("07test")
            .build();

    private SignUpPayload signUpPayload = new SignUpPayload("test", "test", "test", "test");

    @Before
    public void setUp() {
        authController = new AuthController(authenticationManager, tokenProvider, passwordEncoder, userRepository);
    }

    @Test
    public void shouldReturnGreetingMessage() {
        Assert.assertThat(authController.authenticateUser(), Is.is("hi from your service finder platform"));
    }

    @Test
    public void signUpExistingUserResponseBadRequest() {

        BDDMockito.given(this.userRepository.existsByEmail(BDDMockito.any(String.class))).willReturn(true);
        Assert.assertThat(authController.registerUser(signUpPayload),
                Is.is(new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                        HttpStatus.BAD_REQUEST)));
    }

    @Test
    public void signUpExistingUserResponseSuccessful() {

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/Car/{usernameOrEmail}")
                .buildAndExpand(user.getEmail()).toUri();

        BDDMockito.given(this.userRepository.existsByEmail(BDDMockito.any(String.class))).willReturn(false);
        BDDMockito.given(this.userRepository.save(BDDMockito.any(User.class))).willReturn(this.user);
        Assert.assertThat(authController.registerUser(signUpPayload),
                Is.is(ResponseEntity.created(location)
                        .body(new ApiResponse(true, "User registered successfully"))));
    }

}
