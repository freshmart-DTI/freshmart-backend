package com.freshmart.backend.discount.service;

import com.freshmart.backend.discount.dto.DiscountDto;

import java.util.List;

public interface DiscountService {
    List<DiscountDto> getAllDiscounts();
    DiscountDto createDiscount(DiscountDto discountDto);
}
