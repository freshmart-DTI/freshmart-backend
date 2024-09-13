package com.freshmart.backend.inventory.service.impl;

import com.freshmart.backend.inventory.entity.Inventory;
import com.freshmart.backend.inventory.repository.InventoryRepository;
import com.freshmart.backend.inventory.service.InventoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public List<Inventory> getAllInventories(Long storeId) {
        if(storeId == null) {
            inventoryRepository.findAll();
        }
        return inventoryRepository.findByStoreId(storeId);
    }

    @Override
    public Inventory getInventoryById() {
        return null;
    }

    @Override
    public void deleteInventory() {

    }
}
