package com.freshmart.backend.discount.service.impl;

import com.freshmart.backend.discount.dto.DiscountDto;
import com.freshmart.backend.discount.entity.Discount;
import com.freshmart.backend.discount.repository.DiscountRepository;
import com.freshmart.backend.discount.service.DiscountService;
import com.freshmart.backend.product.entity.Product;
import com.freshmart.backend.product.service.impl.ProductServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        return discountRepository.findAll()
                .stream()
                .map(Discount::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DiscountDto getDiscountById(Long discountId) {
        Discount discount = discountRepository.findById(discountId).orElseThrow(() -> new EntityNotFoundException("Discount not found with id: " + discountId));

        return discount.toDto();
    }

    @Override
    public DiscountDto createDiscount(DiscountDto discountDto) {
        Discount discount = discountDto.toEntity();

        Product product = productService.getProductById(discountDto.getProductId()).toEntity();
        discount.setProduct(product);

        Discount savedDiscount = discountRepository.save(discount);

        return savedDiscount.toDto();
    }

    @Override
    public DiscountDto updateDiscount(Long discountId, DiscountDto discountDto) {
        Discount existingDiscount = discountRepository.findById(discountId).orElseThrow(() -> new EntityNotFoundException("Discount not found with id: " + discountId));

        existingDiscount.setAmount(discountDto.getAmount());
        existingDiscount.setType(discountDto.getType());
        existingDiscount.setMinPurchase(discountDto.getMinPurchase());
        existingDiscount.setMaxDiscount(discountDto.getMaxDiscount());

        Discount updatedDiscount = discountRepository.save(existingDiscount);

        return updatedDiscount.toDto();
    }

    @Override
    public void deleteDiscount(Long discountId) {
        Discount discount = discountRepository.findById(discountId).orElseThrow(() -> new EntityNotFoundException("Discount not found with id: " + discountId));

        discountRepository.delete(discount);
    }
}
