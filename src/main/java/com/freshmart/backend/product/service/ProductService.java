package com.freshmart.backend.product.service;

import com.freshmart.backend.product.dto.ProductDto;
import com.freshmart.backend.product.entity.Product;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();
}
