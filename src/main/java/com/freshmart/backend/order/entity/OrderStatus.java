package com.freshmart.backend.order.entity;

public enum OrderStatus {
    PENDING_PAYMENT,
    AWAITING_PAYMENT_CONFIRMATION,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
