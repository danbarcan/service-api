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

            user.setEmail(StringUtils.isEmpty(userProfilePayload.getEmail()) ? user.getEmail() : userProfilePayload.getEmail());
            user.setName(StringUtils.isEmpty(userProfilePayload.getName()) ? user.getName() : userProfilePayload.getName());
            user.setUsername(StringUtils.isEmpty(userProfilePayload.getUsername()) ? user.getUsername() : userProfilePayload.getUsername());
            user.setPhoneNumber(StringUtils.isEmpty(userProfilePayload.getPhone()) ? user.getPhoneNumber() : userProfilePayload.getPhone());

            ServiceDetails serviceDetails = user.getServiceDetails();
            serviceDetails.setAddress(StringUtils.isEmpty(userProfilePayload.getServiceAddress()) ? serviceDetails.getAddress() : userProfilePayload.getServiceAddress());
            serviceDetails.setName(StringUtils.isEmpty(userProfilePayload.getServiceName()) ? serviceDetails.getName() : userProfilePayload.getName());
            serviceDetails.setCategories(userProfilePayload.getCategories() == null || userProfilePayload.getCategories().length == 0 ? serviceDetails.getCategories() : getCategoriesFromIdList(categoryRepository, userProfilePayload.getCategories()));
            serviceDetails.setLat(userProfilePayload.getLat() == null ? serviceDetails.getLat() : userProfilePayload.getLat());
            serviceDetails.setLng(userProfilePayload.getLng() == null ? serviceDetails.getLng() : userProfilePayload.getLng());

            user.setPassword(passwordEncoder.encode(StringUtils.isEmpty(userProfilePayload.getEmail()) ? user.getEmail() : userProfilePayload.getPassword()));

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
