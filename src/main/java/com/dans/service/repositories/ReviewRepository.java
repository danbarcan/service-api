package com.dans.service.repositories;

import com.dans.service.entities.Job;
import com.dans.service.entities.Review;
import com.dans.service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "review", path = "review")
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findById(Long id);

    List<Review> findAll();

    List<Review> findAllByService(User service);

    List<Review> findAllByUser(User user);

    List<Review> findAllByJob(Job job);

    @Query("select avg(r.rating + 0.0) from Review r where r.service = :service")
    Double getRating(User service);
}
