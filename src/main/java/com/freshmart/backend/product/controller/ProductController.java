package com.freshmart.backend.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshmart.backend.product.dto.ProductDto;
import com.freshmart.backend.product.service.ProductService;
import com.freshmart.backend.product.service.impl.ProductServiceImpl;
import com.freshmart.backend.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public ResponseEntity<?> getAllProducts() {
        List<ProductDto> productDtos = productService.getAllProducts();
        return Response.success("List of products fetched", productDtos);
    }

    @PostMapping()
    public ResponseEntity<?> createProduct(@RequestPart("productDto") String productDtoJson, @RequestPart("images") List<MultipartFile> images) throws Exception {

        ProductDto productDto = objectMapper.readValue(productDtoJson, ProductDto.class);
        ProductDto newProduct = productService.createProduct(productDto, images);

        return Response.success(HttpStatus.CREATED.value(), "Product created successfully", newProduct);
    }
}
