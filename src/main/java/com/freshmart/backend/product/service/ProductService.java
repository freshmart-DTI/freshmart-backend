package com.freshmart.backend.product.service;

import com.freshmart.backend.product.dto.ProductDto;
import com.freshmart.backend.product.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();
    ProductDto createProduct(ProductDto productDto, List<MultipartFile> images) throws Exception;
}
