package com.freshmart.backend.product.dto;

import com.freshmart.backend.product.entity.Product;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private Long categoryId;

    public Product toEntity() {
        Product product = new Product();

        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);

        return product;
    }
}
