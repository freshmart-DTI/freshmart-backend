package com.freshmart.backend.discount.controller;

import com.freshmart.backend.discount.dto.DiscountDto;
import com.freshmart.backend.discount.entity.Discount;
import com.freshmart.backend.discount.service.impl.DiscountServiceImpl;
import com.freshmart.backend.response.Response;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/discounts")
@Validated
public class DiscountController {
    private final DiscountServiceImpl discountService;

    public DiscountController(DiscountServiceImpl discountService) {
        this.discountService = discountService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllDiscounts() {
        List<DiscountDto> discounts = discountService.getAllDiscounts();

        return Response.success("List of discounts fetched", discounts);
    }

    @PostMapping()
    public ResponseEntity<?> createDiscount(@Valid @RequestBody DiscountDto discountDto) {
        DiscountDto createdDiscount = discountService.createDiscount(discountDto);

        return Response.success("Product created successfully", createdDiscount);
    }
}
