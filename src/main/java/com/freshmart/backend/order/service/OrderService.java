package com.freshmart.backend.order.service;

import com.freshmart.backend.order.dto.OrderDto;
import com.freshmart.backend.response.PagedResponse;


public interface OrderService {
    PagedResponse<OrderDto> getAllOrders(int page, int size);
    OrderDto createOrder(OrderDto orderDto);
}
