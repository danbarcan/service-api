package com.dans.service.entities;

import com.dans.service.repositories.CategoryRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "categories")
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String description;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(mappedBy = "categories")
    private Set<Job> jobCategories;

    public static Set<Category> getCategoriesFromIdList(CategoryRepository categoryRepository, Long[] categoryIds) {
        return Arrays.stream(categoryIds).map(cat -> {
            Optional<Category> categoryOptional = categoryRepository.findById(cat);

            if (categoryOptional.isPresent()) {
                return categoryOptional.get();
            } else {
                return null;
            }
        }).collect(Collectors.toSet());
    }
}
