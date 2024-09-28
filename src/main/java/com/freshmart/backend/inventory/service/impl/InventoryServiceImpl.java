package com.freshmart.backend.inventory.service.impl;

import com.freshmart.backend.inventory.dto.InventoryDto;
import com.freshmart.backend.inventory.entity.Inventory;
import com.freshmart.backend.inventory.entity.InventoryJournal;
import com.freshmart.backend.inventory.repository.InventoryRepository;
import com.freshmart.backend.inventory.service.InventoryService;
import com.freshmart.backend.product.entity.Product;
import com.freshmart.backend.product.service.impl.ProductServiceImpl;
import com.freshmart.backend.store.entity.Store;
import com.freshmart.backend.store.service.impl.StoreServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ProductServiceImpl productService;
    private final StoreServiceImpl storeService;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, ProductServiceImpl productService, StoreServiceImpl storeService) {
        this.inventoryRepository = inventoryRepository;
        this.productService = productService;
        this.storeService = storeService;
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
    @Transactional
    public Inventory createInventory(InventoryDto inventoryDto) {
        Inventory inventory = new Inventory();

        Product product = productService.getProductById(inventoryDto.getProductId()).toEntity();
        inventory.setProduct(product);
        inventory.setQuantity(inventoryDto.getQuantity());

        Store store = storeService.getStoreById(inventoryDto.getProductId());
        inventory.setStore(store);

        InventoryJournal inventoryJournal = new InventoryJournal();
        inventoryJournal.setQuantityChange(inventoryDto.getQuantity());
        inventoryJournal.setInventory(inventory);

        List<InventoryJournal> inventoryJournals = new ArrayList<>();
        inventoryJournals.add(inventoryJournal);
        inventory.setInventoryJournals(inventoryJournals);

        Inventory savedRepository = inventoryRepository.save(inventory);

        return savedRepository;
    }

    @Override
    public InventoryDto getInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Inventory with id: " + id + " not found"));
        return inventory.toDto();
    }

    @Override
    public Inventory getInventoryByStoreAndProduct(Store store, Product product) {
        return inventoryRepository.findByStoreAndProduct(store, product).orElseThrow(() -> new EntityNotFoundException("Inventory not found"));
    }

    @Override
    public Inventory updateInventory(Long inventoryId, InventoryDto inventoryDto) {
        Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(() -> new EntityNotFoundException("Inventory with id: " + inventoryId + " not found"));

        int quantityChange = inventoryDto.getQuantity() - inventory.getQuantity();

        Product product = productService.getProductById(inventoryDto.getProductId()).toEntity();
        inventory.setProduct(product);
        inventory.setQuantity(inventoryDto.getQuantity());

        Store store = storeService.getStoreById(inventoryDto.getStoreId());
        inventory.setStore(store);

        InventoryJournal inventoryJournal = new InventoryJournal();
        inventoryJournal.setQuantityChange(quantityChange);
        inventoryJournal.setInventory(inventory);
        inventory.getInventoryJournals().add(inventoryJournal);

        Inventory savedRepository = inventoryRepository.save(inventory);

        return savedRepository;
    }

    @Override
    public void deleteInventory() {

    }
}
