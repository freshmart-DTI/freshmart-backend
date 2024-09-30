package com.freshmart.backend.inventory.repository;

import com.freshmart.backend.inventory.entity.Inventory;
import com.freshmart.backend.product.entity.Product;
import com.freshmart.backend.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByStoreId(Long storeId);
    Optional<Inventory> findByStoreAndProduct(Store store, Product product);
}
