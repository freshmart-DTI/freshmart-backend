package com.freshmart.backend.discount.repository;

import com.freshmart.backend.discount.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
