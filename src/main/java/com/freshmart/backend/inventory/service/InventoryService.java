package com.freshmart.backend.inventory.service;

import com.freshmart.backend.inventory.dto.InventoryDto;
import com.freshmart.backend.inventory.entity.Inventory;

import java.util.List;

public interface InventoryService {
    List<InventoryDto> getAllInventories(Long storeId);
    Inventory createInventory(InventoryDto inventoryDto);
    Inventory getInventoryById();
    void deleteInventory();
}
