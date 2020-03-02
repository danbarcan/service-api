package com.dans.service.services;

import com.dans.service.entities.ServiceDetails;
import com.dans.service.entities.User;
import com.dans.service.entities.UserProfile;
import com.dans.service.payloads.ServiceProfilePayload;
import com.dans.service.payloads.UserProfilePayload;
import com.dans.service.payloads.UserSummary;
import com.dans.service.repositories.CategoryRepository;
import com.dans.service.repositories.UserRepository;
import com.dans.service.security.UserPrincipal;
import com.dans.service.utils.ImageUtils;
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

    public Boolean updateServiceDetails(ServiceProfilePayload serviceProfilePayload) {
        Optional<User> userOptional = userRepository.findById(serviceProfilePayload.getId());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            updateServiceDetails(user.getServiceDetails(), serviceProfilePayload);

            userRepository.save(user);

            return true;
        }

        return false;
    }

    public UserProfile getUserDetails(Long id) {
        Optional<User> user = userRepository.findById(id);

        return user.map(UserProfile::createUserProfileFromUser).orElse(null);
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
    }

    private void updateServiceDetails(ServiceDetails serviceDetails, ServiceProfilePayload serviceProfilePayload) {
        if (!StringUtils.isEmpty(serviceProfilePayload.getServiceAddress())) {
            serviceDetails.setAddress(serviceProfilePayload.getServiceAddress());
        }
        if (!StringUtils.isEmpty(serviceProfilePayload.getServiceName())) {
            serviceDetails.setName(serviceProfilePayload.getServiceName());
        }
        if (serviceProfilePayload.getCategories() != null) {
            serviceDetails.setCategories(getCategoriesFromIdList(categoryRepository, serviceProfilePayload.getCategories()));
        }
        if (serviceProfilePayload.getLat() != null) {
            serviceDetails.setLat(serviceProfilePayload.getLat());
        }
        if (serviceProfilePayload.getLng() != null) {
            serviceDetails.setLng(serviceProfilePayload.getLng());
        }

        if (serviceProfilePayload.getImage() != null) {
            serviceDetails.setImage(ImageUtils.base64StringToByteArray(serviceProfilePayload.getImage()));
        }
    }
}
