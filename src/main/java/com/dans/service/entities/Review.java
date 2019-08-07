package com.dans.service.entities;

import com.dans.service.payloads.ReviewPayload;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private User service;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @NotBlank
    private String description;

    @NotNull
    private Integer rating;

    @NotNull
    @Column(columnDefinition="Timestamp default current_timestamp")
    private Timestamp timestamp;

    public static Review createReviewFromPayload(ReviewPayload reviewPayload, User service, User user, Job job) {
        return Review.builder()
                .description(reviewPayload.getDescription())
                .job(job)
                .service(service)
                .user(user)
                .rating(reviewPayload.getRating())
                .build();
    }

    public Review updateReviewFromPayload(ReviewPayload reviewPayload) {
        this.setDescription(reviewPayload.getDescription());
        this.setRating(reviewPayload.getRating());
        return this;
    }
}
