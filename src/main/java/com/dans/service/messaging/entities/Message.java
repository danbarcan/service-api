package com.dans.service.messaging.entities;

import com.dans.service.entities.Job;
import com.dans.service.entities.Offer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect
public class Message implements Serializable {
    private MessageType messageType;

    private String emailAddress;
    private String username;
    private String name;

    private Job job;
    private Offer offer;
}
