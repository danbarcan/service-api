package com.dans.service.test.controllers;

import com.dans.service.configs.WebMvcConfig;
import com.dans.service.controllers.UserController;
import com.dans.service.entities.Role;
import com.dans.service.entities.RoleName;
import com.dans.service.entities.User;
import com.dans.service.payloads.UserSummary;
import com.dans.service.security.UserPrincipal;
import com.dans.service.services.UserService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

@Ignore
@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
@ContextConfiguration(classes = {WebMvcConfig.class})
public class UserControllerSecurityTest {

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private WebApplicationContext context;

    private User user = User.builder().email("test@test.com")
            .password("password")
            .name("test")
            .username("test")
            .phoneNumber("07test")
            .role(Role.builder().name(RoleName.ROLE_USER).build())
            .build();

    private UserPrincipal currentUser = UserPrincipal.create(user);
    private UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getUsername(), currentUser.getAuthorities().iterator().next().getAuthority());

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .build();
    }

    @Test
    public void getUserWithNoUserIsUnauthorized() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/me").accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Ignore("Until we figure out how to make the request with specified roles")
    @Test
    @WithMockUser(username = "username", roles = "SERVICE")
    public void getUserWithRoleUserIsOk() throws Exception {

        //BDDMockito.given(this.userService.getCurrentUser(currentUser)).willReturn(userSummary);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/me").accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
