package com.freshmart.backend.inventory.service;

import com.freshmart.backend.inventory.dto.InventoryDto;
import com.freshmart.backend.inventory.entity.Inventory;
import com.freshmart.backend.product.entity.Product;
import com.freshmart.backend.store.entity.Store;

import java.util.List;

public interface InventoryService {
    List<InventoryDto> getAllInventories(Long storeId);
    Inventory createInventory(InventoryDto inventoryDto);
    InventoryDto getInventoryById(Long id);
    Inventory getInventoryByStoreAndProduct(Store store, Product product);
    Inventory updateInventory(Long inventoryId, InventoryDto inventoryDto);
    Inventory updateQuantity(Inventory inventory, int quantityChange);
    void deleteInventory();
}
