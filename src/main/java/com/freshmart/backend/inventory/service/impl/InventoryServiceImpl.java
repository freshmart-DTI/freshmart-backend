package com.freshmart.backend.inventory.service.impl;

import com.freshmart.backend.inventory.dto.InventoryDto;
import com.freshmart.backend.inventory.entity.Inventory;
import com.freshmart.backend.inventory.repository.InventoryRepository;
import com.freshmart.backend.inventory.service.InventoryService;
import com.freshmart.backend.product.entity.Product;
import com.freshmart.backend.product.service.impl.ProductServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ProductServiceImpl productService;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, ProductServiceImpl productService) {
        this.inventoryRepository = inventoryRepository;
        this.productService = productService;
    }

    @Override
    public List<InventoryDto> getAllInventories(Long storeId) {
        List<Inventory> inventories = new ArrayList<>();
        if(storeId != null) {
            inventories = inventoryRepository.findByStoreId(storeId);
        } else {
            inventories = inventoryRepository.findAll();
        }
        List<InventoryDto> inventoryDtos = inventories.stream().map(Inventory::toDto).collect(Collectors.toList());

        return inventoryDtos;
    }

    @Override
    public Inventory createInventory(InventoryDto inventoryDto) {
        Inventory inventory = new Inventory();

        Product product = productService.getProductById(inventoryDto.getProductId()).toEntity();
        inventory.setProduct(product);
        inventory.setQuantity(inventoryDto.getQuantity());
        inventory.setStoreId(inventoryDto.getStoreId());

        Inventory savedRepository = inventoryRepository.save(inventory);

        return savedRepository;
    }

    @Override
    public Inventory getInventoryById() {
        return null;
    }

    @Override
    public void deleteInventory() {

    }
}
