package com.dans.service.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobPayload {

    @NotBlank
    private String make;

    @NotBlank
    private String model;

    @NotBlank
    @Size(min = 4, max = 4)
    private String year;

    @NotBlank
    private String description;

    private String location;

}
