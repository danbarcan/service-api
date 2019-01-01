package com.dans.service.repositories;

import com.dans.service.entities.ServiceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "service", path = "service")
public interface ServiceDetailsRepository extends JpaRepository<ServiceDetails, Long> {
}
