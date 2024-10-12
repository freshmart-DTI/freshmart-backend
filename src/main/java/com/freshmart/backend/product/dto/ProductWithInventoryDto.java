package com.freshmart.backend.product.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductWithInventoryDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private List<ProductImageDto> images;
    private Long categoryId;
    private Integer stock;
}
