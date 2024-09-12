package com.freshmart.backend.product.dto;

import com.freshmart.backend.product.entity.Product;
import com.freshmart.backend.product.entity.ProductImage;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private List<ProductImageDto> productImages;
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
