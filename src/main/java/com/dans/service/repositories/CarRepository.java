package com.dans.service.repositories;

import com.dans.service.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    @RestResource(rel = "find-car-by-id", path = "find-car-by-id")
    Optional<Car> findById(Long pollId);

    @RestResource(rel = "find-cars", path = "find-cars")
    List<Car> findAll();
}
