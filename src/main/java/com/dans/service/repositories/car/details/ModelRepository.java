package com.dans.service.repositories.car.details;

import com.dans.service.entities.car.details.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
    List<Model> findAllByManufacturer_Id(long manufacturerId);
}
