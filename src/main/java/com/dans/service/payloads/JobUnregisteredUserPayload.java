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
public class JobUnregisteredUserPayload {

    private Long categoryId;

    @NotNull
    private Long detailsId;

    @NotBlank
    private String description;

    @NotBlank
    private String email;

    @NotNull
    private BigDecimal lat;

    @NotNull
    private BigDecimal lng;

    @NotEmpty
    private Long[] categories;
}
