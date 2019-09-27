package com.dans.service.repositories;

import com.dans.service.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "categories", path = "categories")
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findById(Long id);
}
