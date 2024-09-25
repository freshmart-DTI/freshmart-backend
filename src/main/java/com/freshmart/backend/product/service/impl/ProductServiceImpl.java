package com.freshmart.backend.product.service.impl;

import com.freshmart.backend.product.dto.ProductDto;
import com.freshmart.backend.product.entity.Category;
import com.freshmart.backend.product.entity.Product;
import com.freshmart.backend.product.entity.ProductImage;
import com.freshmart.backend.product.repository.ProductRepository;
import com.freshmart.backend.product.repository.ProductSpecification;
import com.freshmart.backend.product.service.ProductService;
import com.freshmart.backend.response.PagedResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryServiceImpl categoryService;
    private final ImageUploadServiceImpl imageUploadService;

    public ProductServiceImpl(ProductRepository productRepository, CategoryServiceImpl categoryService, ImageUploadServiceImpl imageUploadService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.imageUploadService = imageUploadService;
    }

    @Override
    public PagedResponse<ProductDto> getAllProducts(String search, Double minPrice, Double maxPrice, String category, int page, int size, String sortBy, String sortDir) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        Specification<Product> spec = Specification.where(ProductSpecification.withSearch(search))
                .and(ProductSpecification.withMinPrice(minPrice))
                .and(ProductSpecification.withMaxPrice(maxPrice))
                .and(ProductSpecification.withCategory(category));

        Page<Product> productPage = productRepository.findAll(spec, pageable);

        List<ProductDto> productDtos = productPage.getContent().stream()
                .map(Product::toDto)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                productDtos,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast()
        );
    }

    @Override
    public ProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Product with id: " + productId + " not found"));

        return product.toDto();
    }

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto productDto, List<MultipartFile> images) throws Exception {
        Product product = productDto.toEntity();

        Category category = categoryService.getCategoryById(productDto.getCategoryId());
        product.setCategory(category);

        List<ProductImage> productImages = new ArrayList<>();

        for(MultipartFile image : images) {
            String imageUrl = imageUploadService.uploadImage(image, "products");

            ProductImage productImage = new ProductImage();
            productImage.setUrl(imageUrl);
            productImage.setProduct(product);
            productImages.add(productImage);
        }

        product.setProductImages(productImages);

        Product savedProduct = productRepository.save(product);

        return savedProduct.toDto();
    }

    @Override
    public void deleleProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Product with id: " + productId + " not found"));

        productRepository.delete(product);
    }
}
