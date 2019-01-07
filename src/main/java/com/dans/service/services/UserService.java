package com.dans.service.services;

import com.dans.service.payloads.UserSummary;
import com.dans.service.repositories.UserRepository;
import com.dans.service.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
