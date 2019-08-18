package com.dans.service.test.controllers;

import com.dans.service.controllers.UserController;
import com.dans.service.entities.Role;
import com.dans.service.entities.RoleName;
import com.dans.service.entities.User;
import com.dans.service.entities.UserProfile;
import com.dans.service.payloads.UserIdentityAvailability;
import com.dans.service.payloads.UserProfilePayload;
import com.dans.service.payloads.UserSummary;
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
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@Ignore
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Mock
    private UserService userService;

    private UserController userController;

    private User user = User.builder().email("test@test.com")
            .password("password")
            .name("test")
            .username("test")
            .phoneNumber("07test")
            .role(Role.builder().name(RoleName.ROLE_USER).build())
            .build();

    @Mock
    private UserProfile userProfile;

    @Mock
    private UserProfilePayload userProfilePayload;

    @Before
    public void setUp() {
        userController = new UserController(userService);
    }

    @Test
    public void getCurrentUserReturnsOk() {
        UserPrincipal currentUser = UserPrincipal.create(user);
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getUsername(), currentUser.getAuthorities().iterator().next().getAuthority());

        BDDMockito.given(this.userService.getCurrentUser(currentUser)).willReturn(userSummary);
        Assert.assertThat(userController.getCurrentUser(currentUser), Is.is(userSummary));
    }

    @Test
    public void checkUsernameAvailabilityReturnTrue() {
        String username = "test";

        BDDMockito.given(this.userService.checkUsernameAvailability(username)).willReturn(true);
        Assert.assertThat(userController.checkUsernameAvailability(username), Is.is(new UserIdentityAvailability(true)));
    }

    @Test
    public void checkUsernameAvailabilityReturnFalse() {
        String username = "test";

        BDDMockito.given(this.userService.checkUsernameAvailability(username)).willReturn(false);
        Assert.assertThat(userController.checkUsernameAvailability(username), Is.is(new UserIdentityAvailability(false)));
    }

    @Test
    public void checkEmailAvailabilityReturnTrue() {
        String email = "email@test.com";

        BDDMockito.given(this.userService.checkEmailAvailability(email)).willReturn(true);
        Assert.assertThat(userController.checkEmailAvailability(email), Is.is(new UserIdentityAvailability(true)));
    }

    @Test
    public void checkEmailAvailabilityReturnFalse() {
        String email = "email@test.com";

        BDDMockito.given(this.userService.checkEmailAvailability(email)).willReturn(false);
        Assert.assertThat(userController.checkEmailAvailability(email), Is.is(new UserIdentityAvailability(false)));
    }

    @Test
    public void getUserProfile() {
        BDDMockito.given(this.userService.getUserDetails(BDDMockito.anyLong())).willReturn(userProfile);

        Assert.assertThat(this.userController.getUserProfile(1L), Is.is(userProfile));
    }

    @Test
    public void updateUserProfile() {
        BDDMockito.given(this.userService.updateUserDetails(BDDMockito.any(UserProfilePayload.class))).willReturn(true);

        Assert.assertTrue(this.userController.updateUserProfile(userProfilePayload));
    }

}
