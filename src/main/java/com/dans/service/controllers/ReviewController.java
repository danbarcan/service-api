package com.dans.service.controllers;

import com.dans.service.entities.Review;
import com.dans.service.payloads.ApiResponse;
import com.dans.service.payloads.ReviewPayload;
import com.dans.service.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ReviewController {

    private ReviewService reviewService;

    @Autowired
    public ReviewController(final ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/users/review")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> saveReview(@Valid @RequestBody ReviewPayload reviewPayload) {
        return reviewService.saveReview(reviewPayload);
    }

    @PostMapping("/users/updateReview")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> updateReview(@Valid @RequestBody ReviewPayload reviewPayload) {
        return reviewService.updateReview(reviewPayload);
    }

    @GetMapping("/users/deleteReview")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> deleteReview(@RequestParam Long reviewId) {
        return reviewService.deleteReview(reviewId);
    }

    @GetMapping("/users/allReviews")
    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN') OR hasRole('SERVICE')")
    public ResponseEntity<List<Review>> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/users/allReviewsByUser")
    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN') OR hasRole('SERVICE')")
    public ResponseEntity<List<Review>> getAllReviewsByUser(@RequestParam Long userId) {
        return reviewService.getAllReviewsByUser(userId);
    }

    @GetMapping("/users/allReviewsByService")
    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN') OR hasRole('SERVICE')")
    public ResponseEntity<List<Review>> getAllReviewsByService(@RequestParam Long serviceId) {
        return reviewService.getAllReviewsByService(serviceId);
    }
}
