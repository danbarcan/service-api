package com.dans.service.services;

import com.dans.service.entities.User;
import com.dans.service.entities.UserProfile;
import com.dans.service.payloads.UserProfilePayload;
import com.dans.service.payloads.UserSummary;
import com.dans.service.repositories.UserRepository;
import com.dans.service.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserSummary getCurrentUser(UserPrincipal currentUser) {
        return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getUsername(), currentUser.getAuthorities().iterator().next().getAuthority());
    }

    public Boolean checkUsernameAvailability(String username) {
        return !userRepository.existsByUsername(username);
    }

    public Boolean checkEmailAvailability(String email) {
        return !userRepository.existsByEmail(email);
    }

    public Boolean updateUserDetails(UserProfilePayload userProfilePayload) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((UserPrincipal) auth.getPrincipal()).getId();
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            user.setEmail(userProfilePayload.getEmail());
            user.setName(userProfilePayload.getName());
            user.setUsername(userProfilePayload.getUsername());
            user.setPhoneNumber(userProfilePayload.getPhone());

            user.setPassword(passwordEncoder.encode(userProfilePayload.getNewPassword()));

            userRepository.save(user);

            return true;
        }

        return false;
    }

    public UserProfile getUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findById(((UserPrincipal) auth.getPrincipal()).getId());

        if (user.isPresent()) {
            return UserProfile.createUserProfileFromUser(user.get());
        }

        return null;
    }
}
