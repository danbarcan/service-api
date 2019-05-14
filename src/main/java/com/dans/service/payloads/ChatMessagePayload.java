package com.dans.service.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessagePayload {
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long jobId;

    @NotBlank
    private String message;
}
