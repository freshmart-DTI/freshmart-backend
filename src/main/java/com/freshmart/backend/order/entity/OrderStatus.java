package com.freshmart.backend.order.entity;

public enum OrderStatus {
    AWAITING_PAYMENT,
    AWAITING_PAYMENT_CONFIRMATION,
    PROCESSING,
    SHIPPED,
    CONFIRMED,
    CANCELLED,
}
