package com.dans.service.test.controllers;

import com.dans.service.controllers.AuthController;
import com.dans.service.entities.ServiceDetails;
import com.dans.service.entities.User;
import com.dans.service.payloads.ApiResponse;
import com.dans.service.payloads.LoginPayload;
import com.dans.service.payloads.SignUpPayload;
import com.dans.service.services.AuthService;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    private AuthController authController;

    @Mock
    private AuthService authService;

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

    @Before
    public void setUp() {
        authController = new AuthController(authService);
    }

    @Test
    public void shouldReturnGreetingMessage() {
        Assert.assertThat(authController.authenticateUser(), Is.is("hi from your user finder platform"));
    }

    @Test
    public void signUpExistingUserMailResponseBadRequest() {

        BDDMockito.given(this.authService.registerUser(BDDMockito.any(SignUpPayload.class))).willReturn(new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                HttpStatus.BAD_REQUEST));
        Assert.assertThat(authController.registerUser(signUpPayloadUser),
                Is.is(new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                        HttpStatus.BAD_REQUEST)));
    }

    @Test
    public void signUpExistingUsernameResponseBadRequest() {

        BDDMockito.given(this.authService.registerUser(BDDMockito.any(SignUpPayload.class))).willReturn(new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                HttpStatus.BAD_REQUEST));
        Assert.assertThat(authController.registerUser(signUpPayloadUser),
                Is.is(new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                        HttpStatus.BAD_REQUEST)));
    }

    @Test
    public void signUpUserResponseSuccessful() {

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/Car/{usernameOrEmail}")
                .buildAndExpand(user.getEmail()).toUri();

        BDDMockito.given(this.authController.registerUser(BDDMockito.any(SignUpPayload.class))).willReturn(ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully")));
        Assert.assertThat(authController.registerUser(signUpPayloadUser),
                Is.is(ResponseEntity.created(location)
                        .body(new ApiResponse(true, "User registered successfully"))));
    }

    @Test
    public void signUpServiceResponseSuccessful() {

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/Car/{usernameOrEmail}")
                .buildAndExpand(service.getEmail()).toUri();

        BDDMockito.given(this.authController.registerUser(BDDMockito.any(SignUpPayload.class))).willReturn(ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully")));
        Assert.assertThat(authController.registerUser(signUpPayloadService),
                Is.is(ResponseEntity.created(location)
                        .body(new ApiResponse(true, "User registered successfully"))));
    }

    @Test
    public void signInUserResponseNull() {
        Assert.assertNull(authController.authenticateUser(loginPayload));
    }
}
