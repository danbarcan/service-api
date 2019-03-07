package com.dans.service.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobPayload {
    private Long id;

    @NotNull
    private Long userId;
    
    private Long carId;

    @NotBlank
    private String description;

    private String location;
}
