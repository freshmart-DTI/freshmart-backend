package com.freshmart.backend.order.repository;

import com.freshmart.backend.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
