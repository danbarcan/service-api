package com.dans.service.payloads;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginPayload {
    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;
}
