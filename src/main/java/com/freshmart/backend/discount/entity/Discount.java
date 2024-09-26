package com.freshmart.backend.discount.entity;

import com.freshmart.backend.discount.dto.DiscountDto;
import com.freshmart.backend.product.entity.Product;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discount_id_gen")
    @SequenceGenerator(name = "discount_id_gen", sequenceName = "discount_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private DiscountType type;

    @Column(name = "min_purchase", nullable = false)
    private BigDecimal minPurchase;

    @Column(name = "max_discount", nullable = false)
    private BigDecimal maxDiscount;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @CreationTimestamp
    private Instant createdAt;

    public DiscountDto toDto() {
        DiscountDto discountDto = new DiscountDto();

        discountDto.setId(id);
        discountDto.setAmount(amount);
        discountDto.setProductId(product.getId());
        discountDto.setType(type);
        discountDto.setMinPurchase(minPurchase);
        discountDto.setMaxDiscount(maxDiscount);
        discountDto.setStartDate(startDate);
        discountDto.setEndDate(endDate);

        return discountDto;
    }
}
