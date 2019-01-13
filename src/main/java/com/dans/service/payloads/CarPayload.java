package com.dans.service.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarPayload {

    private Long id;

    @NotBlank
    private String make;

    @NotBlank
    private String model;

    @NotBlank
    @Size(min = 4, max = 4)
    private String year;

    @NotNull
    private Long userId;
}
