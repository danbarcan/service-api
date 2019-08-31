package com.dans.service.services;

import com.dans.service.entities.Job;
import com.dans.service.entities.Review;
import com.dans.service.entities.User;
import com.dans.service.payloads.ApiResponse;
import com.dans.service.payloads.ReviewPayload;
import com.dans.service.repositories.JobRepository;
import com.dans.service.repositories.ReviewRepository;
import com.dans.service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private ReviewRepository reviewRepository;

    private JobRepository jobRepository;

    private UserRepository userRepository;

    @Autowired
    public ReviewService(final ReviewRepository reviewRepository, final JobRepository jobRepository, final UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<ApiResponse> saveReview(ReviewPayload reviewPayload) {

        Optional<Job> jobOptional = jobRepository.findById(reviewPayload.getJobId());
        if (!jobOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Job not found"));
        }

        Job job = jobOptional.get();

        Optional<User> serviceOptional = userRepository.findById(job.getAcceptedService().getId());

        if (!serviceOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "UNAUTHORIZED"));
        }
        Optional<User> user = userRepository.findById(job.getUser().getId());
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "UNAUTHORIZED"));
        }

        User service = serviceOptional.get();

        Review review = Review.createReviewFromPayload(reviewPayload, service, user.get(), jobOptional.get());
        reviewRepository.save(review);

        job.setReview(review);
        jobRepository.save(job);

        service.setRating(reviewRepository.getRating(service));
        userRepository.save(service);

        return ResponseEntity.ok(new ApiResponse(true, "Review successfully saved"));
    }

    public ResponseEntity<ApiResponse> updateReview(ReviewPayload reviewPayload) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewPayload.getId());
        if (!reviewOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Review not found"));
        }

        Review review = reviewOptional.get();
        review.updateReviewFromPayload(reviewPayload);

        reviewRepository.save(review);

        User service = review.getService();
        service.setRating(reviewRepository.getRating(service));
        userRepository.save(service);

        return ResponseEntity.ok(new ApiResponse(true, "Review successfully updated"));
    }

    public ResponseEntity<ApiResponse> deleteReview(Long reviewId) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if (!reviewOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Review not found"));
        }

        Review review = reviewOptional.get();
        reviewRepository.delete(review);

        return ResponseEntity.ok(new ApiResponse(true, "Review successfully deleted"));
    }

    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(reviewRepository.findAll());
    }

    public ResponseEntity<List<Review>> getAllReviewsByUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(reviewRepository.findAllByUser(user.get()));
    }

    public ResponseEntity<List<Review>> getAllReviewsByService(Long serviceId) {
        Optional<User> service = userRepository.findById(serviceId);

        if (!service.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(reviewRepository.findAllByService(service.get()));
    }
}
