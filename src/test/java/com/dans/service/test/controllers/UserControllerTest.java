package com.dans.service.test.controllers;

import com.dans.service.controllers.UserController;
import com.dans.service.payloads.UserSummary;
import com.dans.service.security.UserPrincipal;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Test
    public void getCurrentUserReturnsOk() {
        UserPrincipal currentUser = new UserPrincipal(1L, "test@test.com", "password");
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getUsername());

        Assert.assertThat(userController.getCurrentUser(currentUser), Is.is(userSummary));
    }
}
