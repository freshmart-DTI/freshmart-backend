package com.freshmart.backend.order.dto;

import com.freshmart.backend.order.entity.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class OrderDto {
    private Long id;

    @NotNull(message = "User id is required")
    private Long userId;

    @NotEmpty(message = "Order items must not be empty")
    @Valid
    private List<OrderItemDto> orderItems;

    @NotNull(message = "Status is required")
    private OrderStatus status;

    @NotNull(message = "Total amount is required")
    private BigDecimal totalAmount;

    @NotNull(message = "Discount amount is required")
    private BigDecimal discountAmount;

    @NotNull(message = "Shipping cost is required")
    private BigDecimal shippingCost;

    @NotNull(message = "Final amount is required")
    private BigDecimal finalAmount;

    @NotNull(message = "Address is required")
    private Address shippingAddress;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Payment status is required")
    private PaymentStatus paymentStatus;

    private String paymentProof;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant estimatedDeliveryDate;
    private Instant actualDeliveryDate;

    public Order toEntity() {
        Order order = new Order();

        order.setStatus(status);
        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(discountAmount);
        order.setShippingCost(shippingCost);
        order.setFinalAmount(finalAmount);
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus(paymentStatus);
        order.setPaymentProof(paymentProof);
        order.setEstimatedDeliveryDate(estimatedDeliveryDate);
        order.setActualDeliveryDate(actualDeliveryDate);

        return order;
    }

}
