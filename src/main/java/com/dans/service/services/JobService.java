package com.dans.service.services;

import com.dans.service.entities.Car;
import com.dans.service.entities.Job;
import com.dans.service.entities.PartsType;
import com.dans.service.entities.User;
import com.dans.service.payloads.*;
import com.dans.service.repositories.CarRepository;
import com.dans.service.repositories.JobRepository;
import com.dans.service.repositories.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {

    private JobRepository jobRepository;

    private UserRepository userRepository;

    private CarRepository carRepository;

    private OfferService offerService;

    @Autowired
    public JobService(final JobRepository jobRepository, final UserRepository userRepository,final CarRepository carRepository, final OfferService offerService) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.offerService = offerService;
        this.carRepository = carRepository;
    }

    public ResponseEntity<ApiResponse> saveJob(JobPayload jobPayload) {
        Optional<User> userOptional = userRepository.findById(jobPayload.getUserId());

        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "UNAUTHORIZED"));
        }
        User user = userOptional.get();

        Optional<Car> carOptional = Optional.empty();
        if (jobPayload.getCarId() != null) {
            carOptional = carRepository.findById(jobPayload.getCarId());
        }

        Car car = null;
        if (carOptional.isPresent()) {
            car = carOptional.get();
        } else {
            car = Car.builder()
                    .model(jobPayload.getModel())
                    .make(jobPayload.getMake())
                    .year(jobPayload.getYear())
                    .user(user)
                    .build();

            carRepository.save(car);
        }

        Job job = Job.createJobFromJobPayload(jobPayload, user);
        job.setCar(car);

        jobRepository.save(job);

        return ResponseEntity.ok(new ApiResponse(true, "Job successfully saved"));
    }

    public ResponseEntity<ApiResponse> saveJobUnregisteredUser(JobUnregisteredUserPayload jobPayload) {

        User user = User.builder()
                .username(jobPayload.getEmail())
                .phoneNumber("0")
                .name(jobPayload.getEmail())
                .email(jobPayload.getEmail())
                .password(AuthService.encodePassword(RandomString.make(10)))
                .build();

        Car car = Car.builder()
                .model(jobPayload.getModel())
                .make(jobPayload.getMake())
                .year(jobPayload.getYear())
                .user(user)
                .build();

        Job job = Job.builder()
                .description(jobPayload.getDescription())
                .partsType(PartsType.NEW)
                .location(StringUtils.isEmpty(jobPayload.getLocation()) ? "location" : jobPayload.getLocation()) //TODO remove this mock location
                .timestamp(Timestamp.from(Instant.now()))
                .user(user)
                .car(car)
                .build();

        userRepository.save(user);
        carRepository.save(car);
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
        return hideOrUnhideJob(offerId, true);
    }

    public ResponseEntity<ApiResponse> unhideJob(Long offerId) {
        return hideOrUnhideJob(offerId, false);
    }

    public ResponseEntity<ApiResponse> hideOrUnhideJob(Long offerId, boolean hide) {
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

        if (hide) {
            job.addUserToHiddenList(user);
        } else {
            job.removeUserToHiddenList(user);
        }
        jobRepository.save(job);

        return ResponseEntity.ok(new ApiResponse(true, "Job successfully " + (hide ? "hidden" : "unhidden")));
    }

    private List<JobResponsePayload> getAllJobResponsePayloads(List<Job> jobs, User user) {
        return jobs.stream().map(job -> JobResponsePayload.createJobResponsePayloadFromJob(job, user)).collect(Collectors.toList());
    }
}
