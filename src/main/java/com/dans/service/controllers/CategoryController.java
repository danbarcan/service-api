package com.dans.service.controllers;

import com.dans.service.entities.Category;
import com.dans.service.payloads.ApiResponse;
import com.dans.service.payloads.CategoryPayload;
import com.dans.service.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/admin/saveCategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> saveCategory(@Valid @RequestBody CategoryPayload categoryPayload) {
        return categoryService.saveCategory(categoryPayload);
    }

    @PostMapping("/admin/updateCategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> updateCategory(@Valid @RequestBody CategoryPayload categoryPayload) {
        return categoryService.updateCategory(categoryPayload);
    }

    @GetMapping("/admin/deleteCategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> deleteCategory(@RequestParam Long categoryId) {
        return categoryService.deleteCategory(categoryId);
    }

    @GetMapping("/users/allCategories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
