package com.freshmart.backend.order.service.impl;

import com.freshmart.backend.order.dto.OrderDto;
import com.freshmart.backend.order.dto.OrderItemDto;
import com.freshmart.backend.order.entity.Order;
import com.freshmart.backend.order.entity.OrderItem;
import com.freshmart.backend.order.repository.OrderRepository;
import com.freshmart.backend.order.service.OrderService;
import com.freshmart.backend.product.entity.Product;
import com.freshmart.backend.product.service.impl.ProductServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductServiceImpl productService;

    public OrderServiceImpl(OrderRepository orderRepository, ProductServiceImpl productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = orderDto.toEntity();

        List<OrderItem> orderItems = orderDto.getOrderItems().stream().map(orderItemDto -> {
            OrderItem orderItem = orderItemDto.toEntity();

            Product product = productService.getProductById(orderItemDto.getProductId()).toEntity();
            orderItem.setProduct(product);

            orderItem.setOrder(order);

            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);

        Order newOrder = orderRepository.save(order);

        OrderDto newOrderDto = newOrder.toDto();

        List<OrderItemDto> newOrderItemDtos = newOrder.getOrderItems().stream()
                .map(OrderItem::toDto)
                .collect(Collectors.toList());


        newOrderDto.setOrderItems(newOrderItemDtos);

        return newOrderDto;
    }
}
