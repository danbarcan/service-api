package com.dans.service.test.services;

import com.dans.service.entities.Role;
import com.dans.service.entities.RoleName;
import com.dans.service.entities.User;
import com.dans.service.entities.UserProfile;
import com.dans.service.payloads.UserProfilePayload;
import com.dans.service.payloads.UserSummary;
import com.dans.service.repositories.UserRepository;
import com.dans.service.security.UserPrincipal;
import com.dans.service.services.UserService;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp() {
        userService = new UserService(userRepository, passwordEncoder);
    }

    private User user = User.builder().email("test@test.com")
            .password("password")
            .name("test")
            .username("test")
            .phoneNumber("07test")
            .role(Role.builder().name(RoleName.ROLE_USER).build())
            .build();

    private UserProfilePayload userProfilePayload = UserProfilePayload.childBuilder()
            .id(1L)
            .cui("12345")
            .email("test@test.com")
            .name("test")
            .oldPassword("oldpass")
            .password("newpass")
            .phone("0121212120")
            .username("test")
            .build();

    @Test
    public void getCurrentUserReturnsOk() {
        UserPrincipal currentUser = UserPrincipal.create(user);
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getUsername(), currentUser.getAuthorities().iterator().next().getAuthority());

        Assert.assertThat(userService.getCurrentUser(currentUser), Is.is(userSummary));
    }

    @Test
    public void checkUsernameAvailabilityReturnTrue() {
        String username = "test";

        BDDMockito.given(this.userRepository.existsByUsername(username)).willReturn(false);
        Assert.assertTrue(userService.checkUsernameAvailability(username));
    }

    @Test
    public void checkUsernameAvailabilityReturnFalse() {
        String username = "test";

        BDDMockito.given(this.userRepository.existsByUsername(username)).willReturn(true);
        Assert.assertFalse(userService.checkUsernameAvailability(username));
    }

    @Test
    public void checkEmailAvailabilityReturnTrue() {
        String email = "email@test.com";

        BDDMockito.given(this.userRepository.existsByEmail(email)).willReturn(false);
        Assert.assertTrue(userService.checkEmailAvailability(email));
    }

    @Test
    public void checkEmailAvailabilityReturnFalse() {
        String email = "email@test.com";

        BDDMockito.given(this.userRepository.existsByEmail(email)).willReturn(true);
        Assert.assertFalse(userService.checkEmailAvailability(email));
    }

    @Test
    public void updateUserDetailsReturnsTrue() {
        BDDMockito.given(this.userRepository.findById(BDDMockito.any())).willReturn(Optional.of(user));

        Assert.assertTrue(userService.updateUserDetails(userProfilePayload));
    }

    @Test
    public void updateUserDetailsReturnsFalse() {
        BDDMockito.given(this.userRepository.findById(BDDMockito.any())).willReturn(Optional.empty());

        Assert.assertFalse(userService.updateUserDetails(userProfilePayload));
    }

    @Test
    public void getUserDetailsReturnsUserDetails() {
        BDDMockito.given(this.userRepository.findById(BDDMockito.any())).willReturn(Optional.of(user));

        Assert.assertThat(userService.getUserDetails(user.getId()), Is.is(UserProfile.createUserProfileFromUser(user)));
    }

    @Test
    public void getUserDetailsReturnsNull() {
        BDDMockito.given(this.userRepository.findById(BDDMockito.any())).willReturn(Optional.empty());

        Assert.assertNull(userService.getUserDetails(user.getId()));
    }
}
