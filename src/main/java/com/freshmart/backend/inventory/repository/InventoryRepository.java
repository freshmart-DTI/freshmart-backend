package com.freshmart.backend.inventory.repository;

import com.freshmart.backend.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
