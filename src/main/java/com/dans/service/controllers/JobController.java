package com.dans.service.controllers;

import com.dans.service.entities.Job;
import com.dans.service.payloads.ApiResponse;
import com.dans.service.payloads.JobPayload;
import com.dans.service.payloads.JobResponsePayload;
import com.dans.service.payloads.OfferPayload;
import com.dans.service.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/users/deleteJob")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> deleteJob(@RequestParam Long jobId) {
        return jobService.deleteJob(jobId);
    }

    @GetMapping("/services/jobs")
    @PreAuthorize("hasRole('ROLE_SERVICE')")
    public ResponseEntity<List<JobResponsePayload>> getAllJobs() {
        return jobService.getAllJobs();
    }

    @GetMapping("/users/jobs")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<JobResponsePayload>> getAllJobs(@RequestParam Long userId) {
        return jobService.getAllJobs(userId);
    }

    @GetMapping("/services/acceptJob")
    @PreAuthorize("hasRole('ROLE_SERVICE')")
    public ResponseEntity<ApiResponse> acceptJob(@Valid @RequestBody OfferPayload offerPayload) {
        return jobService.acceptJob(offerPayload);
    }

    @GetMapping("/services/job")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Job> getJob(@RequestParam Long jobId) {
        return jobService.getJobById(jobId);
    }

    @GetMapping("/services/offers/hideJob")
    @PreAuthorize("hasRole('ROLE_SERVICE')")
    public ResponseEntity<ApiResponse> hideJob(@RequestParam Long jobId) {
        return jobService.hideJob(jobId);
    }
}
