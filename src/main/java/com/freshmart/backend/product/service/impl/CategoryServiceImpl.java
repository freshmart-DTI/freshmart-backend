package com.freshmart.backend.product.service.impl;

import com.freshmart.backend.product.dto.CategoryDto;
import com.freshmart.backend.product.entity.Category;
import com.freshmart.backend.product.repository.CategoryRepository;
import com.freshmart.backend.product.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category with ID " + id + " not found"));
    }

    @Override
    public Category createCategory(CategoryDto categoryDto) {
        Category category = categoryDto.toEntity();

        return categoryRepository.save(category);
    }

    @Override
    public Category editCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category with id: " + id + " not found"));

        category.setName(categoryDto.getName());

        return categoryRepository.save(category);
    }
}
