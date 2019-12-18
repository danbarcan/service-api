package com.dans.service.test.repositories;

import com.dans.service.entities.Car;
import com.dans.service.entities.Job;
import com.dans.service.entities.PartsType;
import com.dans.service.entities.User;
import com.dans.service.entities.car.details.Details;
import com.dans.service.entities.car.details.Manufacturer;
import com.dans.service.repositories.JobRepository;
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
public class JobRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JobRepository jobRepository;

    private Details details = Details.builder().build();

    private User user = User.builder().email("test@test.com")
            .password("password")
            .name("test")
            .username("test")
            .phoneNumber("07test")
            .build();

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

    @Test
    public void findAllShouldReturnJobs() {
        this.entityManager.persist(user);
        this.entityManager.persist(details);
        this.entityManager.persist(car);
        this.entityManager.persist(job);
        List<Job> jobs = jobRepository.findAll();
        Assertions.assertThat(jobs != null && jobs.size() == 1).isTrue();
    }

    @Test
    public void findAllShouldReturnNull() {
        List<Job> jobs = jobRepository.findAll();
        Assertions.assertThat(jobs != null && jobs.size() == 1).isFalse();
    }

    @Test
    public void findByIdShouldReturnJob() {
        this.entityManager.persist(user);
        this.entityManager.persist(details);
        this.entityManager.persist(car);
        this.entityManager.persist(job);
        List<Job> jobs = this.jobRepository.findAll();
        Job job1 = jobs.get(0);
        Optional<Job> job = jobRepository.findById(job1.getId());
        Assertions.assertThat(job.isPresent()).isTrue();
        Assertions.assertThat(job.get().equals(job1)).isTrue();
    }

    @Test
    public void findByIdShouldReturnNull() {
        Optional<Job> job = jobRepository.findById(-1L);
        Assertions.assertThat(job.isPresent()).isFalse();
    }
}
