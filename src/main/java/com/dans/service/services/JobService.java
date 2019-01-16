package com.dans.service.services;

import com.dans.service.entities.Job;
import com.dans.service.entities.User;
import com.dans.service.payloads.ApiResponse;
import com.dans.service.payloads.JobPayload;
import com.dans.service.repositories.JobRepository;
import com.dans.service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private JobRepository jobRepository;

    private UserRepository userRepository;

    @Autowired
    public JobService(final JobRepository jobRepository, final UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<ApiResponse> saveJob(JobPayload jobPayload) {
        Optional<User> user = userRepository.findById(jobPayload.getUserId());

        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "UNAUTHORIZED"));
        }

        Job job = Job.createJobFromJobPayload(jobPayload, user.get());

        jobRepository.save(job);

        return ResponseEntity.ok(new ApiResponse(true, "Job successfully saved"));
    }

    public ResponseEntity<ApiResponse> updateJob(JobPayload jobPayload) {
        Optional<Job> jobOptional = jobRepository.findById(jobPayload.getId());
        if (!jobOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Job not found"));
        }

        Job job = jobOptional.get();
        job.updateFieldsWithPayloadData(jobPayload);

        jobRepository.save(job);

        return ResponseEntity.ok(new ApiResponse(true, "Job successfully updated"));
    }

    public ResponseEntity<ApiResponse> deleteJob(Long jobId) {
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (!jobOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Job not found"));
        }

        Job job = jobOptional.get();
        jobRepository.delete(job);

        return ResponseEntity.ok(new ApiResponse(true, "Job successfully deleted"));
    }

    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobRepository.findAll());
    }
}
