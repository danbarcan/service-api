package com.dans.service.controllers;

import com.dans.service.entities.UserProfile;
import com.dans.service.payloads.UserIdentityAvailability;
import com.dans.service.payloads.UserProfilePayload;
import com.dans.service.payloads.UserSummary;
import com.dans.service.security.CurrentUser;
import com.dans.service.security.UserPrincipal;
import com.dans.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN') OR hasRole('SERVICE')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return userService.getCurrentUser(currentUser);
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        return new UserIdentityAvailability(userService.checkUsernameAvailability(username));
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        return new UserIdentityAvailability(userService.checkEmailAvailability(email));
    }

    @GetMapping("/user/profile")
    @PreAuthorize("authentication.id = id")
    public UserProfile getUserProfile(@RequestParam Long id) {
        return userService.getUserDetails(id);
    }

    @PostMapping("/user/updateProfile")
    //@PreAuthorize("authentication.id = userProfilePayload.getId()")
    public Boolean updateUserProfile(@Valid @RequestBody UserProfilePayload userProfilePayload) {
        return userService.updateUserDetails(userProfilePayload);
    }
}
