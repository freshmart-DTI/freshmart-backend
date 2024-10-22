package com.freshmart.backend.order.controller;

import com.freshmart.backend.order.dto.OrderCreateDto;
import com.freshmart.backend.order.dto.OrderDto;
import com.freshmart.backend.order.entity.Order;
import com.freshmart.backend.order.service.impl.OrderServiceImpl;
import com.freshmart.backend.response.PagedResponse;
import com.freshmart.backend.response.Response;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@Validated
public class OrderController {
    private final OrderServiceImpl orderService;

    public OrderController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllOrders(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        PagedResponse<OrderDto> orderDtos = orderService.getAllOrders(page, size);
        return Response.success("List of orders fetched", orderDtos);
    }

    @PostMapping()
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderCreateDto orderCreateDto) {
        OrderDto order = orderService.createOrder(orderCreateDto);
        return Response.success("Order created successfully", order);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable("orderId") Long orderId) {
        OrderDto orderDto = orderService.cancelOrder(orderId);
        return Response.success("Order with id: " + orderId + " canceled", orderDto);
    }

    @PutMapping("/{orderId}/confirm")
    public ResponseEntity<?> confirmOrder(@PathVariable("orderId") Long orderId) {
        OrderDto orderDto = orderService.confirmOrder(orderId);
        return Response.success("Order with id: " + orderId + " confirmed", orderDto);
    }

    @PutMapping("/{orderId}/ship")
    public ResponseEntity<?> shipOrder(@PathVariable("orderId") Long orderId) {
        OrderDto orderDto = orderService.shipOrder(orderId);
        return Response.success("Order with id: " + orderId + " confirmed");
    }
}
