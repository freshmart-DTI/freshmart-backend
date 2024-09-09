package com.freshmart.backend.product.service.impl;

import com.freshmart.backend.product.dto.CategoryDto;
import com.freshmart.backend.product.entity.Category;
import com.freshmart.backend.product.repository.CategoryRepository;
import com.freshmart.backend.product.service.CategoryService;
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
    public Category createCategory(CategoryDto categoryDto) {
        Category category = categoryDto.toEntity();

        return categoryRepository.save(category);
    }
}
