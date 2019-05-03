package com.dans.service.services;

import com.dans.service.entities.Job;
import com.dans.service.entities.Offer;
import com.dans.service.entities.User;
import com.dans.service.payloads.ApiResponse;
import com.dans.service.payloads.JobPayload;
import com.dans.service.payloads.JobResponsePayload;
import com.dans.service.payloads.OfferPayload;
import com.dans.service.repositories.JobRepository;
import com.dans.service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {

    private JobRepository jobRepository;

    private UserRepository userRepository;

    private OfferService offerService;

    @Autowired
    public JobService(final JobRepository jobRepository, final UserRepository userRepository, final OfferService offerService) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.offerService = offerService;
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

    public ResponseEntity<List<JobResponsePayload>> getAllJobs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> userOptional = userRepository.findByEmailOrUsername(userDetails.getUsername(), userDetails.getUsername());
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        User user = userOptional.get();
        List<Job> jobs = jobRepository.findAllByOrderByTimestampDesc();

        return ResponseEntity.ok(getAllJobResponsePayloads(jobs, user));
    }

    public ResponseEntity<List<JobResponsePayload>> getAllJobs(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        User user = userOptional.get();
        List<Job> jobs = jobRepository.findAllByUserIdOrderByTimestampDesc(userId);
        return ResponseEntity.ok(getAllJobResponsePayloads(jobs, user));
    }

    public ResponseEntity<ApiResponse> acceptJob(OfferPayload acceptJobPayload) {
        //TODO: send mail to client
        return offerService.saveOffer(acceptJobPayload);
    }

    public ResponseEntity<Job> getJobById(Long jobId) {
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (!jobOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(jobOptional.get());
    }

    public ResponseEntity<ApiResponse> hideJob(Long offerId) {
        Optional<Job> jobOptional = jobRepository.findById(offerId);
        if (!jobOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Job not found"));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> userOptional = userRepository.findByEmailOrUsername(userDetails.getUsername(), userDetails.getUsername());

        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "User not authenticated"));
        }
        User user = userOptional.get();
        Job job = jobOptional.get();
        job.addUserToHiddenList(user);
        jobRepository.save(job);

        return ResponseEntity.ok(new ApiResponse(true, "Offer successfully deleted"));
    }

    private List<JobResponsePayload> getAllJobResponsePayloads(List<Job> jobs, User user) {
        return jobs.stream().map(job -> JobResponsePayload.createJobResponsePayloadFromJob(job, user)).collect(Collectors.toList());
    }
}
