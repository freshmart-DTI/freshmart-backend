package com.freshmart.backend.discount.entity;

import com.freshmart.backend.product.entity.Product;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "vouchers")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "voucher_id_gen")
    @SequenceGenerator(name = "voucher_id_gen", sequenceName = "voucher_id_seq")
    private Long id;

    @Column(name = "type", nullable = false)
    private VoucherType voucherType;

    @Column(name = "discount_type", nullable = false)
    private VoucherDiscountType voucherDiscountType;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "max_discount", nullable = false)
    private BigDecimal maxDiscount;

    @Column(name = "expired_date", nullable = false)
    private LocalDate expiredDate;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
