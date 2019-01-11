package com.dans.service.controllers;

import com.dans.service.entities.Job;
import com.dans.service.payloads.ApiResponse;
import com.dans.service.payloads.JobPayload;
import com.dans.service.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class JobController {

    private JobService jobService;

    @Autowired
    public JobController(final JobService jobService) {
        this.jobService = jobService;
    }


    @PostMapping("/users/job")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> saveJob(@Valid @RequestBody JobPayload jobPayload) {
        return jobService.saveJob(jobPayload);
    }

    @PostMapping("/users/updateJob")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> updateJob(@Valid @RequestBody JobPayload jobPayload) {
        return jobService.updateJob(jobPayload);
    }

    @GetMapping("/services/jobs")
    @PreAuthorize("hasRole('ROLE_SERVICE')")
    public ResponseEntity<List<Job>> getAllJobs() {
        return jobService.getAllJobs();
    }
}
