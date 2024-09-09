package com.freshmart.backend.product.controller;

import com.freshmart.backend.product.dto.ProductDto;
import com.freshmart.backend.product.service.ProductService;
import com.freshmart.backend.product.service.impl.ProductServiceImpl;
import com.freshmart.backend.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@Validated
public class ProductController {
    private final ProductServiceImpl productService;

    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllProducts() {
        List<ProductDto> productDtos = productService.getAllProducts();
        return Response.success("List of products fetched", productDtos);
    }
}
