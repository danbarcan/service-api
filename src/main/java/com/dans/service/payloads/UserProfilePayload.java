package com.dans.service.payloads;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserProfilePayload extends SignUpPayload {

    @NotNull
    private Long id;

    @NotBlank
    @Size(min = 6, max = 20)
    private String oldPassword;

    @Builder(builderMethodName = "childBuilder")
    public UserProfilePayload(String name, String username, String email, String phone, String password, String serviceName, String serviceAddress, String cui, Long id, String oldPassword) {
        super(name, username, email, phone, password, serviceName, serviceAddress, cui);
        this.id = id;
        this.oldPassword = oldPassword;
    }
}
