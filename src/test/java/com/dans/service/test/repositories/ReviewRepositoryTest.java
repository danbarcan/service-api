package com.dans.service.test.repositories;

import com.dans.service.entities.*;
import com.dans.service.entities.car.details.Details;
import com.dans.service.repositories.ReviewRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ReviewRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReviewRepository reviewRepository;

    private ServiceDetails serviceDetails = ServiceDetails.builder()
            .address("test")
            .cui(111L)
            .name("ServiceDetails")
            .lat(BigDecimal.ZERO)
            .lng(BigDecimal.ZERO)
            .build();

    private User service = User.builder().email("service@test.com")
            .password("password")
            .name("test")
            .username("service")
            .phoneNumber("07test")
            .serviceDetails(serviceDetails)
            .build();

    private User user = User.builder().email("test@test.com")
            .password("password")
            .name("test")
            .username("test")
            .phoneNumber("07test")
            .build();

    private Details details = Details.builder().build();

    private Car car = Car.builder()
            .details(details)
            .user(user)
            .build();

    private Job job = Job.builder()
            .user(user)
            .description("description")
            .lat(new BigDecimal(42.4566))
            .lng(new BigDecimal(32.4566))
            .partsType(PartsType.NEW)
            .timestamp(Timestamp.from(Instant.now()))
            .car(car)
            .build();

    private Review review = Review.builder()
            .service(service)
            .user(user)
            .job(job)
            .timestamp(Timestamp.from(Instant.now()))
            .description("description")
            .rating(5)
            .build();

    @Test
    public void findAllShouldReturnReviews() {
        this.entityManager.persist(service);
        this.entityManager.persist(user);
        this.entityManager.persist(details);
        this.entityManager.persist(car);
        this.entityManager.persist(job);
        this.entityManager.persist(review);
        List<Review> reviews = reviewRepository.findAll();
        Assertions.assertThat(reviews != null && reviews.size() == 1).isTrue();
    }

    @Test
    public void findAllShouldReturnNull() {
        List<Review> reviews = reviewRepository.findAll();
        Assertions.assertThat(reviews != null && reviews.size() == 1).isFalse();
    }

    @Test
    public void findByIdShouldReturnReview() {
        this.entityManager.persist(service);
        this.entityManager.persist(user);
        this.entityManager.persist(details);
        this.entityManager.persist(car);
        this.entityManager.persist(job);
        this.entityManager.persist(review);
        List<Review> reviews = reviewRepository.findAll();
        Review review1 = reviews.get(0);
        Optional<Review> review = reviewRepository.findById(review1.getId());
        Assertions.assertThat(review.isPresent()).isTrue();
        Assertions.assertThat(review.get().equals(review1)).isTrue();
    }

    @Test
    public void findByIdShouldReturnNull() {
        Optional<Review> review = reviewRepository.findById(-1L);
        Assertions.assertThat(review.isPresent()).isFalse();
    }
}
