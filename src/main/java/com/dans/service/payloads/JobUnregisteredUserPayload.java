package com.dans.service.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobUnregisteredUserPayload {

    @NotBlank
    private String make;

    private Long categoryId;

    @NotBlank
    private String model;

    @NotNull
    private Integer year;

    @NotBlank
    private String description;

    @NotBlank
    private String email;

    private String location;
}
