package com.dans.service.controllers;

import com.dans.service.payloads.ApiResponse;
import com.dans.service.payloads.JwtAuthenticationResponse;
import com.dans.service.payloads.LoginPayload;
import com.dans.service.payloads.SignUpPayload;
import com.dans.service.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;

    @Autowired
    public AuthController(final AuthService authService) {
        this.authService = authService;
    }


    @GetMapping("/hello")
    public String authenticateUser() {
        return "hi from your user finder platform";
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginPayload loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpPayload signUpPayload) {
        return authService.registerUser(signUpPayload);
    }
}