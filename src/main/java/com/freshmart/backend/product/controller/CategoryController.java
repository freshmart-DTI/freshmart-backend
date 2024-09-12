package com.freshmart.backend.product.controller;

import com.freshmart.backend.product.dto.CategoryDto;
import com.freshmart.backend.product.entity.Category;
import com.freshmart.backend.product.service.impl.CategoryServiceImpl;
import com.freshmart.backend.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
@Validated
public class CategoryController {
    private final CategoryServiceImpl categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return Response.success("List of categories fetched", categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") Long id) {
        Category category = categoryService.getCategoryById(id);
        return Response.success("Category details retrieved successfully", category);
    }

    @PostMapping()
    public ResponseEntity<?> createCategory(@RequestBody CategoryDto categoryDto) {
        Category category = categoryService.createCategory(categoryDto);
        return Response.success(HttpStatus.CREATED.value(), "Category created successfully", category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id, @RequestBody CategoryDto categoryDto) {
        Category category = categoryService.editCategory(id, categoryDto);
        return Response.success("Category with id: " + id + " updated", category);
    }
}
