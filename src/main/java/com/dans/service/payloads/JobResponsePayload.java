package com.dans.service.payloads;

import com.dans.service.entities.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class JobResponsePayload {
    private Long id;

    private String description;

    private PartsType partsType;

    private User acceptedService;

    private String location;

    private Timestamp timestamp;

    private Car car;

    private User user;

    private Set<Offer> offers;

    private JobState jobState;

    private Review review;

    private BigDecimal lat;

    private BigDecimal lng;

    private Set<Category> categories;

    public static JobResponsePayload createJobResponsePayloadFromJob(Job job, User user) {
        return JobResponsePayload.builder()
                .id(job.getId())
                .description(job.getDescription())
                .partsType(PartsType.NEW)
                .acceptedService(job.getAcceptedService())
                .lat(job.getLat())
                .lng(job.getLng())
                .timestamp(Timestamp.from(Instant.now()))
                .user(job.getUser())
                .offers(job.getOffers())
                .jobState(JobState.getState(job, user))
                .car(job.getCar())
                .review(job.getReview())
                .categories(job.getCategories())
                .build();
    }
}
