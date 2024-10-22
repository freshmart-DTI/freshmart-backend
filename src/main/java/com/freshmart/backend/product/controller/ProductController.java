package com.freshmart.backend.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshmart.backend.product.dto.ProductDto;
import com.freshmart.backend.product.entity.Product;
import com.freshmart.backend.product.service.ProductService;
import com.freshmart.backend.product.service.impl.ProductServiceImpl;
import com.freshmart.backend.response.PagedResponse;
import com.freshmart.backend.response.Response;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/products")
@Validated
public class ProductController {
    private final ProductServiceImpl productService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllProducts(@RequestParam(required = false) String search,
                                            @RequestParam(required = false) Double minPrice,
                                            @RequestParam(required = false) Double maxPrice,
                                            @RequestParam(required = false) String category,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "id") String sortBy,
                                            @RequestParam(defaultValue = "asc") String sortDir) {

        PagedResponse<ProductDto> products = productService.getAllProducts(search, minPrice, maxPrice, category, page, size, sortBy, sortDir);

        return Response.success("List of products fetched", products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable("productId") Long productId) {
        ProductDto productDto = productService.getProductById(productId);
        return Response.success("Product details retrieved successfully", productDto);
    }

    @PostMapping()
    public ResponseEntity<?> createProduct(@RequestPart("productDto") String productDtoJson, @RequestPart("images") List<MultipartFile> images) throws Exception {

        ProductDto productDto = objectMapper.readValue(productDtoJson, ProductDto.class);
        ProductDto newProduct = productService.createProduct(productDto, images);

        return Response.success(HttpStatus.CREATED.value(), "Product created successfully", newProduct);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable("productId") Long productId, @RequestPart("productDto") String productDtoJson, @RequestPart("newImages") List<MultipartFile> newImages, @RequestPart("imagesToRemove") String imagesToRemoveJson) throws Exception {

        List<Long> imagesToRemove = imagesToRemoveJson != null
                ? objectMapper.readValue(imagesToRemoveJson, new TypeReference<List<Long>>() {})
                : null;

        ProductDto productDto = objectMapper.readValue(productDtoJson, ProductDto.class);
        ProductDto updatedProduct = productService.updateProduct(productId, productDto, newImages, imagesToRemove);

        return Response.success("Product updated successfully", updatedProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleleProduct(productId);
        return Response.success("Product deleted successfully");
    }
}
