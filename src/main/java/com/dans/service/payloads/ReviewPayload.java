package com.dans.service.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewPayload {
    private Long id;

    @NotNull
    private Long jobId;

    @NotBlank
    private String description;

    @NotNull
    private Integer rating;

}
