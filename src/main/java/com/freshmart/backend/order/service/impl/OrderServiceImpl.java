package com.freshmart.backend.order.service.impl;

import com.freshmart.backend.inventory.entity.Inventory;
import com.freshmart.backend.inventory.entity.InventoryJournal;
import com.freshmart.backend.inventory.service.impl.InventoryServiceImpl;
import com.freshmart.backend.order.dto.OrderCreateDto;
import com.freshmart.backend.order.dto.OrderDto;
import com.freshmart.backend.order.dto.OrderItemDto;
import com.freshmart.backend.order.entity.Order;
import com.freshmart.backend.order.entity.OrderItem;
import com.freshmart.backend.order.entity.OrderStatus;
import com.freshmart.backend.order.entity.PaymentStatus;
import com.freshmart.backend.order.repository.OrderRepository;
import com.freshmart.backend.order.service.OrderService;
import com.freshmart.backend.product.dto.ProductDto;
import com.freshmart.backend.product.entity.Product;
import com.freshmart.backend.product.service.impl.ProductServiceImpl;
import com.freshmart.backend.response.PagedResponse;
import com.freshmart.backend.store.entity.Store;
import com.freshmart.backend.store.service.impl.StoreServiceImpl;
import com.freshmart.backend.users.entity.User;
import com.freshmart.backend.users.service.impl.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductServiceImpl productService;
    private final InventoryServiceImpl inventoryService;
    private final StoreServiceImpl storeService;
    private final UserServiceImpl userService;

    public OrderServiceImpl(OrderRepository orderRepository, ProductServiceImpl productService, InventoryServiceImpl inventoryService, StoreServiceImpl storeService, UserServiceImpl userService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.inventoryService = inventoryService;
        this.storeService = storeService;
        this.userService = userService;
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
    public OrderDto createOrder(OrderCreateDto orderDto) {
        Order order = orderDto.toEntity();

        User user = userService.getCurrentUser();
        order.setUser(user);

        order.setStatus(OrderStatus.AWAITING_PAYMENT);
        order.setPaymentStatus(PaymentStatus.PENDING);

        Store store = storeService.getStoreById(orderDto.getStoreId());
        order.setStore(store);

        List<OrderItem> orderItems = orderDto.getOrderItems().stream().map(orderItemDto -> {
            OrderItem orderItem = orderItemDto.toEntity();

            Product product = productService.getProductById(orderItemDto.getProductId()).toEntity();
            orderItem.setProduct(product);

            Inventory inventory = inventoryService.getInventoryByStoreAndProduct(store, product);
            if (inventory.getQuantity() < orderItem.getQuantity()) {
                throw new IllegalArgumentException("Insufficient inventory for product: " + product.getName());
            }
            int quantityChange = orderItem.getQuantity() * -1;
            inventoryService.updateQuantity(inventory, quantityChange);

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

    @Override
    public OrderDto cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Cannot cancel order that has been shipped or delivered");
        }

        order.setStatus(OrderStatus.CANCELLED);

        Store store = order.getStore();
        if(store == null) {
            throw new IllegalStateException("Order is not associated with any store");
        }

        for (OrderItem item: order.getOrderItems()) {
            Product product = item.getProduct();
            int quantityToReturn = item.getQuantity();

            Inventory inventory = inventoryService.getInventoryByStoreAndProduct(store, product);
            inventoryService.updateQuantity(inventory, quantityToReturn);
        }

        Order updatedOrder = orderRepository.save(order);
        return updatedOrder.toDto();
    }

    @Override
    public OrderDto confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        order.setStatus(OrderStatus.PROCESSING);

        return order.toDto();
    }

    @Override
    public OrderDto shipOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        order.setStatus(OrderStatus.SHIPPED);

        return order.toDto();
    }
}
