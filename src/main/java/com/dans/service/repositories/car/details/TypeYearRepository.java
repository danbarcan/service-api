package com.dans.service.repositories.car.details;

import com.dans.service.entities.car.details.TypeYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeYearRepository extends JpaRepository<TypeYear, Long> {
    List<TypeYear> findAllByModel_Id(long modelId);
}
