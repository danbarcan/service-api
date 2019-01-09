package com.dans.service.entities;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserProfile {

    private String name;

    private String username;

    private String email;

    private String phoneNumber;

    private Double rating;

    private ServiceDetails serviceDetails;

    private List<Review> reviews;

    private List<Job> jobs;

    private List<Offer> offers;

    private List<Car> cars;


    public static UserProfile createUserProfileFromUser(User user) {
        return UserProfile.builder()
                .cars(user.getCars())
                .email(user.getEmail())
                .jobs(user.getJobs())
                .name(user.getName())
                .offers(user.getOffers())
                .phoneNumber(user.getPhoneNumber())
                .rating(user.getRating())
                .reviews(user.getReviews())
                .serviceDetails(user.getServiceDetails())
                .username(user.getUsername())
                .build();
    }
}