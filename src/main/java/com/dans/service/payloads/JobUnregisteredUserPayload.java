package com.dans.service.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobUnregisteredUserPayload {

    @NotBlank
    private String carBrand;

    @NotBlank
    private String carModel;

    @NotBlank
    private Integer carYear;

    @NotBlank
    private String description;

    @NotBlank
    private String userEmail;

    private String location;
}
