package com.dans.service.repositories;

import com.dans.service.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    Optional<Car> findById(Long carId);

    List<Car> findAll();

    @Query("select c from Car c where c.user.id = :userId")
    List<Car> findAllByUserId(Long userId);
}
