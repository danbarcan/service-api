package com.dans.service.services;

import com.dans.service.entities.*;
import com.dans.service.exception.AppException;
import com.dans.service.messaging.Publisher;
import com.dans.service.messaging.entities.Message;
import com.dans.service.messaging.entities.MessageType;
import com.dans.service.payloads.ApiResponse;
import com.dans.service.payloads.JwtAuthenticationResponse;
import com.dans.service.payloads.LoginPayload;
import com.dans.service.payloads.SignUpPayload;
import com.dans.service.repositories.CategoryRepository;
import com.dans.service.repositories.PasswordTokenRepository;
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
import java.util.Optional;
import java.util.UUID;

import static com.dans.service.entities.Category.getCategoriesFromIdList;

@Service
public class AuthService {
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private CategoryRepository categoryRepository;
    private PasswordTokenRepository passwordTokenRepository;
    private Publisher publisher;

    @Autowired
    public AuthService(final AuthenticationManager authenticationManager, final JwtTokenProvider jwtTokenProvider,
                       final PasswordEncoder passwordEncoder, final UserRepository userRepository,
                       final RoleRepository roleRepository, final CategoryRepository categoryRepository,
                       final PasswordTokenRepository passwordTokenRepository, final Publisher publisher) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
        this.passwordTokenRepository = passwordTokenRepository;
        this.publisher = publisher;
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
        if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpPayload.getEmail()))) {
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpPayload.getUsername()))) {
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

        user.setPassword(encodePassword(user.getPassword()));

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
                    .lat(signUpPayload.getLat())
                    .lng(signUpPayload.getLng())
                    .categories(getCategoriesFromIdList(categoryRepository, signUpPayload.getCategories()))
                    .build();
            user.setServiceDetails(serviceDetails);
        } else {
            userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new AppException("User Role not set."));
        }

        user.setRole(userRole);

        User result = userRepository.save(user);

        publisher.produceMsg(createMessage(user));

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{usernameOrEmail}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

    public void resetPassword(String email) {
        Optional<User> userOptional = userRepository.findByEmailOrUsername(email, email);
        if (!userOptional.isPresent()) {
            return;
        }
        User user = userOptional.get();
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .build();
        passwordResetToken.setExpiryDate(24);
        passwordTokenRepository.save(passwordResetToken);
        publisher.produceMsg(createResetPasswordMessage(passwordResetToken));
    }

    public ResponseEntity<ApiResponse> changePassword(String password, String token) {
        Optional<PasswordResetToken> optionalPasswordResetToken = passwordTokenRepository.findByToken(token);
        if (!optionalPasswordResetToken.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(false, "Token not found!"),
                    HttpStatus.BAD_REQUEST);
        }
        PasswordResetToken passwordResetToken = optionalPasswordResetToken.get();
        User user = passwordResetToken.getUser();
        String updatedPassword = encodePassword(password);
        user.setPassword(updatedPassword);
        userRepository.save(user);
        passwordTokenRepository.delete(passwordResetToken);

        return new ResponseEntity<>(new ApiResponse(true, "Password changed successfully"), HttpStatus.OK);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private Message createMessage(User user) {
        return Message.builder()
                .messageType(MessageType.REGISTER)
                .emailAddress(user.getEmail())
                .name(user.getName())
                .username(user.getUsername())
                .build();
    }

    private Message createResetPasswordMessage(PasswordResetToken passwordResetToken) {
        return Message.builder()
                .messageType(MessageType.RESET_PASSWORD)
                .username(passwordResetToken.getUser().getUsername())
                .name(passwordResetToken.getUser().getName())
                .emailAddress(passwordResetToken.getUser().getEmail())
                .token(passwordResetToken.getToken())
                .build();
    }
}
