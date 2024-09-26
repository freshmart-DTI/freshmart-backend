package com.freshmart.backend.discount.service.impl;

import com.freshmart.backend.discount.dto.DiscountDto;
import com.freshmart.backend.discount.entity.Discount;
import com.freshmart.backend.discount.repository.DiscountRepository;
import com.freshmart.backend.discount.service.DiscountService;
import com.freshmart.backend.product.entity.Product;
import com.freshmart.backend.product.service.impl.ProductServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;
    private final ProductServiceImpl productService;

    public DiscountServiceImpl(DiscountRepository discountRepository, ProductServiceImpl productService) {
        this.discountRepository = discountRepository;
        this.productService = productService;
    }

    @Override
    public List<DiscountDto> getAllDiscounts() {
        return List.of();
    }

    @Override
    public DiscountDto createDiscount(DiscountDto discountDto) {
        Discount discount = discountDto.toEntity();

        Product product = productService.getProductById(discountDto.getProductId()).toEntity();
        discount.setProduct(product);

        Discount savedDiscount = discountRepository.save(discount);

        return savedDiscount.toDto();
    }
}
