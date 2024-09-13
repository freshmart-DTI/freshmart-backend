package com.freshmart.backend.inventory.service;

import com.freshmart.backend.inventory.entity.Inventory;

import java.util.List;

public interface InventoryService {
    List<Inventory> getAllInventories();
    Inventory getInventoryById();
    void deleteInventory();
}
