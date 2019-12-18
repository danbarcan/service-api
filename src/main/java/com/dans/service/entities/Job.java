package com.dans.service.entities;

import com.dans.service.payloads.JobPayload;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "job")
public class Job implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String description;

    @NotNull
    private PartsType partsType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id")
    private User acceptedService;

    @NotNull
    private BigDecimal lat;

    @NotNull
    private BigDecimal lng;

    @NotNull
    @Column(columnDefinition = "Timestamp default current_timestamp")
    private Timestamp timestamp;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "job_category",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    Set<Category> categories;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id")
    private Car car;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "review_id")
    private Review review;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "job", cascade = CascadeType.REMOVE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Offer> offers;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "hidden_jobs",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    Set<User> hiddenForUsers;

    public static Job createJobFromJobPayload(JobPayload jobPayload, User user) {
        return Job.builder()
                .description(jobPayload.getDescription())
                .partsType(PartsType.NEW)
                .lat(jobPayload.getLat())
                .lng(jobPayload.getLng())
                .timestamp(Timestamp.from(Instant.now()))
                .user(user)
                .build();
    }

    public Job updateFieldsWithPayloadData(JobPayload jobPayload) {
        this.setDescription(jobPayload.getDescription());
        this.setLat(jobPayload.getLat());
        this.setLng(jobPayload.getLng());
        this.setTimestamp(Timestamp.from(Instant.now()));

        return this;
    }

    public void addUserToHiddenList(User user) {
        this.hiddenForUsers.add(user);
    }

    public void removeUserToHiddenList(User user) {
        this.hiddenForUsers.remove(user);
    }
}
