package com.dans.service.test.repositories;

import com.dans.service.entities.*;
import com.dans.service.repositories.OfferRepository;
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
public class OfferRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OfferRepository offerRepository;

    private User user = User.builder().email("test@test.com")
            .password("password")
            .name("test")
            .username("test")
            .phoneNumber("07test")
            .build();

    private User service = User.builder().email("test1@test.com")
            .password("password")
            .name("test1")
            .username("test1")
            .phoneNumber("07test")
            .build();

    private Car car = Car.builder()
            .make("Mercedes")
            .model("GLA")
            .year(1000)
            .user(user)
            .build();

    private Job job = Job.builder()
            .user(user)
            .description("description")
            .location("location")
            .partsType(PartsType.NEW)
            .timestamp(Timestamp.from(Instant.now()))
            .car(car)
            .build();

    private Offer offer = Offer.builder()
            .user(service)
            .duration(20L)
            .accepted(false)
            .timestamp(Timestamp.from(Instant.now()))
            .job(job)
            .description("description")
            .build();

    @Test
    public void findAllShouldReturnOffers() {
        this.entityManager.persist(user);
        this.entityManager.persist(service);
        this.entityManager.persist(car);
        this.entityManager.persist(job);
        this.entityManager.persist(offer);
        List<Offer> offers = offerRepository.findAll();
        Assertions.assertThat(offers != null && offers.size() == 1).isTrue();
    }

    @Test
    public void findAllShouldReturnNull() {
        List<Offer> offers = offerRepository.findAll();
        Assertions.assertThat(offers != null && offers.size() == 1).isFalse();
    }

    @Test
    public void findByIdShouldReturnOffer() {
        this.entityManager.persist(user);
        this.entityManager.persist(service);
        this.entityManager.persist(car);
        this.entityManager.persist(job);
        this.entityManager.persist(offer);
        List<Offer> offers = this.offerRepository.findAll();
        Offer offer1 = offers.get(0);
        Optional<Offer> offer = offerRepository.findById(offer1.getId());
        Assertions.assertThat(offer.isPresent()).isTrue();
        Assertions.assertThat(offer.get().equals(offer1)).isTrue();
    }

    @Test
    public void findByIdShouldReturnNull() {
        Optional<Offer> offer = offerRepository.findById(-1L);
        Assertions.assertThat(offer.isPresent()).isFalse();
    }
}
