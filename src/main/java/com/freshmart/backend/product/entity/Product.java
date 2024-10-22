package com.freshmart.backend.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.freshmart.backend.discount.entity.Discount;
import com.freshmart.backend.discount.entity.Voucher;
import com.freshmart.backend.inventory.entity.Inventory;
import com.freshmart.backend.order.entity.OrderItem;
import com.freshmart.backend.product.dto.ProductDto;
import com.freshmart.backend.product.dto.ProductImageDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_gen")
    @SequenceGenerator(name = "product_id_gen", sequenceName = "product_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @NotNull
    @NotBlank
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductImage> images;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inventory> inventories;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Discount> discounts;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Voucher> vouchers;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    public ProductDto toDto() {
        ProductDto productDto = new ProductDto();
        productDto.setId(id);
        productDto.setName(name);
        productDto.setPrice(price);
        productDto.setDescription(description);
        productDto.setCategoryId(category.getId());

        List<ProductImageDto> productImageDtos = images.stream().map(ProductImage::toDto)
                .collect(Collectors.toList());

        productDto.setImages(productImageDtos);

        return productDto;
    }
}
