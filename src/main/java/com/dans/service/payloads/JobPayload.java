package com.dans.service.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobPayload {
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private BigDecimal lat;

    @NotNull
    private BigDecimal lng;

    private Long carId;

    @NotBlank
    private String description;

    private String model;

    private String make;

    private Integer year;

    @NotEmpty
    private Long[] categories;
}
