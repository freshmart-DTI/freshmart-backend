package com.freshmart.backend.product.service;

import com.freshmart.backend.product.dto.ProductDto;
import com.freshmart.backend.product.entity.Product;
import com.freshmart.backend.response.PagedResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface ProductService {
    PagedResponse<ProductDto> getAllProducts(String search, Double minPrice, Double maxPrice, String category, int page, int size, String sortBy, String sortDir);
    List<ProductDto> getAllProducts();
    ProductDto getProductById(Long productId);
    ProductDto createProduct(ProductDto productDto, List<MultipartFile> images) throws Exception;
    ProductDto updateProduct(Long productId, ProductDto productDto, List<MultipartFile> newImages, List<Long> imagesToRemove) throws Exception;
    void deleleProduct(Long productId);

}
