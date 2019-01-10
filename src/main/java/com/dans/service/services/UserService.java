package com.dans.service.services;

import com.dans.service.entities.User;
import com.dans.service.entities.UserProfile;
import com.dans.service.payloads.UserProfilePayload;
import com.dans.service.payloads.UserSummary;
import com.dans.service.repositories.UserRepository;
import com.dans.service.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
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
        Optional<User> userOptional = userRepository.findById(userProfilePayload.getId());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            user.setEmail(userProfilePayload.getEmail());
            user.setName(userProfilePayload.getName());
            user.setUsername(userProfilePayload.getUsername());
            user.setPhoneNumber(userProfilePayload.getPhone());

            user.setPassword(passwordEncoder.encode(userProfilePayload.getPassword()));

            userRepository.save(user);

            return true;
        }

        return false;
    }

    public UserProfile getUserDetails(Long id) {
        Optional<User> user = userRepository.findById(id);

        return user.isPresent() ? UserProfile.createUserProfileFromUser(user.get()) : null;
    }
}
