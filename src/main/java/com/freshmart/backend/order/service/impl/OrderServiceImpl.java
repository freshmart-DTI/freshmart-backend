package com.freshmart.backend.order.service.impl;

import com.freshmart.backend.order.dto.OrderDto;
import com.freshmart.backend.order.dto.OrderItemDto;
import com.freshmart.backend.order.entity.Order;
import com.freshmart.backend.order.entity.OrderItem;
import com.freshmart.backend.order.repository.OrderRepository;
import com.freshmart.backend.order.service.OrderService;
import com.freshmart.backend.product.dto.ProductDto;
import com.freshmart.backend.product.entity.Product;
import com.freshmart.backend.product.service.impl.ProductServiceImpl;
import com.freshmart.backend.response.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public PagedResponse<OrderDto> getAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);



        Page<Order> orderPage = orderRepository.findAll(pageable);

        List<OrderDto> orderDtos = orderPage.stream().map(Order::toDto).collect(Collectors.toList());

        return new PagedResponse<>(
                orderDtos,
                orderPage.getNumber(),
                orderPage.getSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                orderPage.isLast()
        );
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
