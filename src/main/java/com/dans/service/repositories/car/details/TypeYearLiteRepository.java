package com.dans.service.repositories.car.details;

import com.dans.service.entities.car.details.TypeYear;
import com.dans.service.entities.car.details.TypeYearLite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeYearLiteRepository extends JpaRepository<TypeYearLite, Long> {
    List<TypeYearLite> findAllByModel_IdOrderByName(long modelId);
}
