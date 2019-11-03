package com.dans.service.payloads;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class UserProfilePayload extends SignUpPayload {
    @NotNull
    private Long id;

    @Size(min = 3, max = 40)
    private String name;

    @Size(min = 3, max = 15)
    private String username;

    @Size(max = 40)
    @Email
    private String email;

    @Size(min = 10, max = 15)
    private String phone;

    @Size(min = 6, max = 20)
    private String password;

    @NotBlank
    @Size(min = 6, max = 20)
    private String oldPassword;

    private String serviceName;

    private String serviceAddress;

    private String cui;

    private BigDecimal lat;

    private BigDecimal lng;

    private Long[] categories;
}
