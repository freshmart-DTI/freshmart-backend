package com.freshmart.backend.discount.dto;

import com.freshmart.backend.discount.entity.Discount;
import com.freshmart.backend.discount.entity.DiscountType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.aspectj.bridge.Message;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DiscountDto {
    private Long id;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    @NotNull(message = "Type is required")
    private DiscountType type;

    @NotNull(message = "Minimal purchase is required")
    private BigDecimal minPurchase;

    @NotNull(message = "Maximal discount is required")
    private BigDecimal maxDiscount;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    public Discount toEntity() {
        Discount discount = new Discount();

        discount.setAmount(amount);
        discount.setType(type);
        discount.setMinPurchase(minPurchase);
        discount.setMaxDiscount(maxDiscount);
        discount.setStartDate(startDate);
        discount.setEndDate(endDate);

        return discount;
    }
}
