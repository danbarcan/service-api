package com.dans.service.controllers;

import com.dans.service.entities.Job;
import com.dans.service.entities.PartsType;
import com.dans.service.entities.User;
import com.dans.service.payloads.ApiResponse;
import com.dans.service.payloads.JobPayload;
import com.dans.service.repositories.JobRepository;
import com.dans.service.repositories.UserRepository;
import com.dans.service.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
public class JobController {

    private JobRepository jobRepository;

    private UserRepository userRepository;

    @Autowired
    public JobController(final JobRepository jobRepository, final UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }


    @PostMapping("/users/job")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> saveJob(@Valid @RequestBody JobPayload jobPayload) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findById(((UserPrincipal) auth.getPrincipal()).getId());

        Job job = Job.builder()
                .description(jobPayload.getDescription())
                .partsType(PartsType.NEW)
                .location(StringUtils.isEmpty(jobPayload.getLocation()) ? "location" : jobPayload.getLocation()) //TODO remove this mock location
                .timestamp(Timestamp.from(Instant.now()))
                .user(user.isPresent() ? user.get() : null)
                .build();

        jobRepository.save(job);

        return ResponseEntity.ok(new ApiResponse(true, "Job successfully saved"));
    }

    @GetMapping("/services/jobs")
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobRepository.findAll());
    }
}
