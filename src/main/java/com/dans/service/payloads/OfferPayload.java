package com.dans.service.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferPayload {
    private Long id;

    @NotNull
    private Long serviceId;

    @NotNull
    private Double cost;

    @NotBlank
    private String duration;

    @NotNull
    private Long jobId;

}
