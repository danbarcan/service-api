package com.dans.service.test.repositories;

import com.dans.service.entities.Offer;
import com.dans.service.entities.Service;
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

    private Service service = Service.builder()
            .mail("test@test.com")
            .password("password")
            .address("test")
            .cui(111L)
            .phoneNumber("07test")
            .name("Service")
            .build();

    private Offer offer = Offer.builder()
            .service(service)
            .duration(20L)
            .accepted(false)
            .timestamp(Timestamp.from(Instant.now()))
            .build();

    @Test
    public void findAllShouldReturnOffers() {
        this.entityManager.persist(service);
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
        this.entityManager.persist(service);
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
