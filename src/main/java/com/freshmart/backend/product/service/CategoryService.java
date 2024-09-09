package com.freshmart.backend.product.service;

import com.freshmart.backend.product.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category createCategory();
}
