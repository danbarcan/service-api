package com.dans.service.services;

import com.dans.service.entities.ServiceDetails;
import com.dans.service.entities.User;
import com.dans.service.entities.UserProfile;
import com.dans.service.payloads.UserProfilePayload;
import com.dans.service.payloads.UserSummary;
import com.dans.service.repositories.CategoryRepository;
import com.dans.service.repositories.UserRepository;
import com.dans.service.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static com.dans.service.entities.Category.getCategoriesFromIdList;

@Service
public class UserService {

    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(final UserRepository userRepository, final CategoryRepository categoryRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
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

            updateUserFields(user, userProfilePayload);

            userRepository.save(user);

            return true;
        }

        return false;
    }

    public UserProfile getUserDetails(Long id) {
        Optional<User> user = userRepository.findById(id);

        return user.isPresent() ? UserProfile.createUserProfileFromUser(user.get()) : null;
    }

    private void updateUserFields(User user, UserProfilePayload userProfilePayload) {
        if (!StringUtils.isEmpty(userProfilePayload.getEmail())) {
            user.setEmail(userProfilePayload.getEmail());
        }
        if (!StringUtils.isEmpty(userProfilePayload.getName())) {
            user.setName(userProfilePayload.getName());
        }
        if (!StringUtils.isEmpty(userProfilePayload.getUsername())) {
            user.setUsername(userProfilePayload.getUsername());
        }
        if (!StringUtils.isEmpty(userProfilePayload.getPhone())) {
            user.setPhoneNumber(userProfilePayload.getPhone());
        }
        if (!StringUtils.isEmpty(userProfilePayload.getPassword())) {
            user.setPassword((passwordEncoder.encode(userProfilePayload.getPassword())));
        }

        updateServiceDetails(user.getServiceDetails(), userProfilePayload);
    }

    private void updateServiceDetails(ServiceDetails serviceDetails, UserProfilePayload userProfilePayload) {
        if (!StringUtils.isEmpty(userProfilePayload.getServiceAddress())) {
            serviceDetails.setAddress(userProfilePayload.getServiceAddress());
        }
        if (!StringUtils.isEmpty(userProfilePayload.getServiceName())) {
            serviceDetails.setName(userProfilePayload.getServiceName());
        }
        if (userProfilePayload.getCategories() == null) {
            serviceDetails.setCategories(getCategoriesFromIdList(categoryRepository, userProfilePayload.getCategories()));
        }
        if (userProfilePayload.getLat() == null) {
            serviceDetails.setLat(userProfilePayload.getLat());
        }
        if (userProfilePayload.getLng() == null) {
            serviceDetails.setLng(userProfilePayload.getLng());
        }
    }
}
