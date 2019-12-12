package com.dans.service.repositories.car.details;

import com.dans.service.entities.car.details.ModelLite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelLiteRepository extends JpaRepository<ModelLite, Long> {
    List<ModelLite> findAllByManufacturer_IdOrderByName(long manufacturerId);
}
