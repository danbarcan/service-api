package com.dans.service.test.repositories;

import com.dans.service.entities.Review;
import com.dans.service.entities.Service;
import com.dans.service.entities.User;
import com.dans.service.repositories.ReviewRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

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

    private Service service = Service.builder()
            .mail("test@test.com")
            .password("password")
            .address("test")
            .cui(111L)
            .phoneNumber("07test")
            .name("Service")
            .build();

    private User user = User.builder().email("test@test.com")
            .password("password")
            .firstName("test")
            .lastName("test")
            .phoneNumber("07test")
            .build();

    private Review review = Review.builder()
            .service(service)
            .user(user)
            .timestamp(Timestamp.from(Instant.now()))
            .byService(true)
            .description("description")
            .rating(5)
            .build();

    @Test
    public void findAllShouldReturnReviews() {
        this.entityManager.persist(service);
        this.entityManager.persist(user);
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
