package com.dans.service.services;

import com.dans.service.entities.Category;
import com.dans.service.payloads.ApiResponse;
import com.dans.service.payloads.CategoryPayload;
import com.dans.service.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ResponseEntity<ApiResponse> saveCategory(CategoryPayload categoryPayload) {
        Category category = Category.builder().description(categoryPayload.getDescription()).build();

        categoryRepository.save(category);

        return ResponseEntity.ok(new ApiResponse(true, "Category successfully saved"));
    }

    public ResponseEntity<ApiResponse> updateCategory(CategoryPayload categoryPayload) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryPayload.getId());
        if (!categoryOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Category not found"));
        }

        Category category = categoryOptional.get();
        category.setDescription(categoryPayload.getDescription());

        categoryRepository.save(category);

        return ResponseEntity.ok(new ApiResponse(true, "Category successfully updated"));
    }

    public ResponseEntity<ApiResponse> deleteCategory(Long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (!categoryOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Category not found"));
        }

        Category category = categoryOptional.get();
        categoryRepository.delete(category);

        return ResponseEntity.ok(new ApiResponse(true, "Category successfully deleted"));
    }

    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryRepository.findAllByOrderByDescription());
    }
}
