package com.freshmart.backend.product.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    private String name;
    private BigDecimal price;
    private String description;
}
