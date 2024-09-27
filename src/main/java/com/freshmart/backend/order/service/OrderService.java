package com.freshmart.backend.order.service;

import com.freshmart.backend.order.dto.OrderDto;
import com.freshmart.backend.order.entity.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();
    OrderDto createOrder(OrderDto orderDto);
}
