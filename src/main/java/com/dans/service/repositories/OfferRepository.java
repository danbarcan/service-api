package com.dans.service.repositories;

import com.dans.service.entities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "offer", path = "offer")
public interface OfferRepository extends JpaRepository<Offer, Long> {
    Optional<Offer> findById(Long id);

    List<Offer> findAll();
}
