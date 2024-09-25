package com.freshmart.backend.product.service;

import com.freshmart.backend.product.dto.ProductDto;
import com.freshmart.backend.product.entity.Product;
import com.freshmart.backend.response.PagedResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    PagedResponse<ProductDto> getAllProducts(String search, Double minPrice, Double maxPrice, String category, int page, int size, String sortBy, String sortDir);
    ProductDto getProductById(Long productId);
    ProductDto createProduct(ProductDto productDto, List<MultipartFile> images) throws Exception;
    void deleleProduct(Long productId);
}
