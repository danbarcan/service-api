package com.dans.service.services;

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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Service
public class AuthService {
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public AuthService(final AuthenticationManager authenticationManager, final JwtTokenProvider jwtTokenProvider,
                       final PasswordEncoder passwordEncoder, final UserRepository userRepository,
                       final RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(LoginPayload loginRequest) {

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

    public ResponseEntity<ApiResponse> registerUser(SignUpPayload signUpPayload) {
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

        user.setRole(userRole);

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{usernameOrEmail}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));

    }
}