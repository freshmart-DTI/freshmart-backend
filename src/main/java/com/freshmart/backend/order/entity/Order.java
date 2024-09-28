package com.freshmart.backend.order.entity;

import com.freshmart.backend.order.dto.OrderDto;
import com.freshmart.backend.store.entity.Store;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_gen")
    @SequenceGenerator(name = "order_id_gen", sequenceName = "order_id_seq", allocationSize = 1)
    private Long id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private BigDecimal discountAmount;

    @Column(nullable = false)
    private BigDecimal shippingCost;

    @Column(nullable = false)
    private BigDecimal finalAmount;

    @Embedded
    private Address shippingAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    private String paymentProof;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    private Instant estimatedDeliveryDate;

    private Instant actualDeliveryDate;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public OrderDto toDto() {
        OrderDto orderDto = new OrderDto();

        orderDto.setId(id);
        orderDto.setStatus(status);
        orderDto.setTotalAmount(totalAmount);
        orderDto.setDiscountAmount(discountAmount);
        orderDto.setShippingCost(shippingCost);
        orderDto.setFinalAmount(finalAmount);
        orderDto.setShippingAddress(shippingAddress);
        orderDto.setPaymentMethod(paymentMethod);
        orderDto.setPaymentStatus(paymentStatus);
        orderDto.setPaymentProof(paymentProof);
        orderDto.setCreatedAt(createdAt);
        orderDto.setUpdatedAt(updatedAt);
        orderDto.setEstimatedDeliveryDate(estimatedDeliveryDate);
        orderDto.setActualDeliveryDate(actualDeliveryDate);

        return orderDto;
    }
}
