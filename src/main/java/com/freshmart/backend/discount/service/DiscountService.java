package com.freshmart.backend.discount.service;

import com.freshmart.backend.discount.dto.DiscountDto;

import java.util.List;

public interface DiscountService {
    List<DiscountDto> getAllDiscounts();
    DiscountDto getDiscountById(Long discountId);
    DiscountDto createDiscount(DiscountDto discountDto);
    DiscountDto updateDiscount(Long discountId, DiscountDto discountDto);
    void deleteDiscount(Long discountId);
}
