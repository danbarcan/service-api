package com.dans.service.repositories;

import com.dans.service.entities.Job;
import com.dans.service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "job", path = "job")
public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> findById(Long id);

    List<Job> findAllByOrderByTimestampDesc();

    List<Job> findAllByUserIdOrderByTimestampDesc(Long userId);

    List<Job> findAllByUserAndAcceptedServiceOrderByTimestampDesc(User user, User acceptedService);
}
