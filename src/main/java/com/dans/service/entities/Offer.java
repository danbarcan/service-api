package com.dans.service.entities;

import com.dans.service.payloads.OfferPayload;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "offers")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Double cost;

    @NotNull
    private Long duration;

    @NotNull
    private Boolean accepted;

    @NotNull
    private String description;

    @NotNull
    private Timestamp timestamp;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    public static Offer createOfferFromPayload(OfferPayload offerPayload, User service, Job job) {
        return Offer.builder()
                .accepted(false)
                .cost(offerPayload.getCost())
                .duration(Long.parseLong(offerPayload.getDuration()))
                .job(job)
                .timestamp(Timestamp.from(Instant.now()))
                .description(offerPayload.getDescription())
                .user(service)
                .build();
    }

    public Offer updateFieldsWithPayloadData(OfferPayload offerPayload) {
        this.setCost(offerPayload.getCost());
        this.setDuration(Long.parseLong(offerPayload.getDuration()));

        return this;
    }
}
