package com.dans.service.controllers;

import com.dans.service.entities.Role;
import com.dans.service.entities.RoleName;
import com.dans.service.entities.ServiceDetails;
import com.dans.service.entities.User;
import com.dans.service.exception.AppException;
import com.dans.service.payloads.ApiResponse;
import com.dans.service.payloads.JwtAuthenticationResponse;
import com.dans.service.payloads.LoginPayload;
import com.dans.service.payloads.SignUpPayload;
import com.dans.service.repositories.RoleRepository;
import com.dans.service.repositories.UserRepository;
import com.dans.service.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public AuthController(final AuthenticationManager authenticationManager, final JwtTokenProvider jwtTokenProvider,
                          final PasswordEncoder passwordEncoder, final UserRepository userRepository,
                          final RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @GetMapping("/hello")
    public String authenticateUser() {
        return "hi from your user finder platform";
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginPayload loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpPayload signUpPayload) {
        if (userRepository.existsByEmail(signUpPayload.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByUsername(signUpPayload.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = User.builder()
                .email(signUpPayload.getEmail())
                .name(signUpPayload.getName())
                .password(signUpPayload.getPassword())
                .username(signUpPayload.getUsername())
                .phoneNumber(signUpPayload.getPhone())
                .build();

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole;

        if (!StringUtils.isEmpty(signUpPayload.getServiceName()) &&
                !StringUtils.isEmpty(signUpPayload.getServiceAddress()) &&
                !StringUtils.isEmpty(signUpPayload.getCui())) {
            userRole = roleRepository.findByName(RoleName.ROLE_SERVICE)
                    .orElseThrow(() -> new AppException("User Role not set."));
            ServiceDetails serviceDetails = ServiceDetails.builder()
                    .name(signUpPayload.getServiceName())
                    .address(signUpPayload.getServiceAddress())
                    .cui(Long.parseLong(signUpPayload.getCui()))
                    .build();
            user.setServiceDetails(serviceDetails);
        } else {
            userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new AppException("User Role not set."));
        }

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/Car/{usernameOrEmail}")
                .buildAndExpand(result.getEmail()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
}
