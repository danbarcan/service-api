package com.dans.service.repositories.car.details;

import com.dans.service.entities.car.details.Details;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailsRepository extends JpaRepository<Details, Long> {
    List<Details> findAllByTypeYear_Id(long typeYearId);
}
