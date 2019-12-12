package com.dans.service.repositories.car.details;

import com.dans.service.entities.car.details.ManufacturerLite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManufacturerLiteRepository extends JpaRepository<ManufacturerLite, Long> {
    List<ManufacturerLite> findAllByOrderByName();
}
