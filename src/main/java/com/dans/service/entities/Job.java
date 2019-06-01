package com.dans.service.entities;

import com.dans.service.payloads.JobPayload;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class Job {
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

    @NotBlank
    private String location;

    @NotNull
    @Column(columnDefinition="Timestamp default current_timestamp")
    private Timestamp timestamp;

    @Transient
    private Car car;

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
                .location(StringUtils.isEmpty(jobPayload.getLocation()) ? "location" : jobPayload.getLocation()) //TODO remove this mock location
                .timestamp(Timestamp.from(Instant.now()))
                .user(user)
                .build();
    }

    public Job updateFieldsWithPayloadData(JobPayload jobPayload) {
        this.setDescription(jobPayload.getDescription());
        this.setLocation(jobPayload.getLocation());
        this.setTimestamp(Timestamp.from(Instant.now()));

        return this;
    }

    public void addUserToHiddenList(User user) {
        this.hiddenForUsers.add(user);
    }
}
