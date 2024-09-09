package com.freshmart.backend.product.service.impl;

import com.freshmart.backend.product.dto.ProductDto;
import com.freshmart.backend.product.entity.Product;
import com.freshmart.backend.product.repository.ProductRepository;
import com.freshmart.backend.product.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(Product::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productDto.toEntity();

        Product newProduct = productRepository.save(product);

        return newProduct.toDto();
    }
}
