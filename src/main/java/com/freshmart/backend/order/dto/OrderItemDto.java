package com.freshmart.backend.order.dto;

import com.freshmart.backend.order.entity.OrderItem;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private Long id;

    @NotNull(message = "Product ID is required")
    private Long productId;

    private Long orderId;

    @NotNull(message = "Quantity is required")
    private int quantity;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotNull(message = "Discount amount is required")
    private BigDecimal discountAmount;

    public OrderItem toEntity() {
        OrderItem orderItem = new OrderItem();

        orderItem.setQuantity(quantity);
        orderItem.setPrice(price);
        orderItem.setDiscountAmount(discountAmount);

        return orderItem;
    }
}
