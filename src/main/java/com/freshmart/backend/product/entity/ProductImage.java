package com.freshmart.backend.product.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Data
@Table(name = "product_images")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_image_id_gen")
    @SequenceGenerator(name = "product_image_id_gen", sequenceName = "product_image_id_seq")
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "url", nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;
}
