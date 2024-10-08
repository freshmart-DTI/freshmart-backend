package com.freshmart.backend.store.service.impl;

import com.freshmart.backend.inventory.entity.Inventory;
import com.freshmart.backend.inventory.service.impl.InventoryServiceImpl;
import com.freshmart.backend.product.dto.ProductWithInventoryDto;
import com.freshmart.backend.product.service.impl.ProductServiceImpl;
import com.freshmart.backend.store.dto.StoreDto;
import com.freshmart.backend.store.dto.StoreWithProductsDto;
import com.freshmart.backend.store.entity.Store;
import com.freshmart.backend.store.repository.StoreRepository;
import com.freshmart.backend.store.service.StoreService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;
    private final ProductServiceImpl productService;
    private final InventoryServiceImpl inventoryService;

    public StoreServiceImpl(StoreRepository storeRepository, ProductServiceImpl productService, InventoryServiceImpl inventoryService) {
        this.storeRepository = storeRepository;
        this.productService = productService;
        this.inventoryService = inventoryService;
    }

    @Override
    public List<Store> getAllStores() {
        return List.of();
    }

    @Override
    public Store createStore(StoreDto storeDto) {
        Store store = storeDto.toEntity();

        if (storeRepository.count() == 0) {
            store.setIsMain(true);
        } else {
            store.setIsMain(false);
        }

        return storeRepository.save(store);
    }

    @Override
    public Store getStoreById(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() -> new EntityNotFoundException("Store not found with id: " + storeId));
    }

    @Override
    public Store findNearestStore(Double latitude, Double longitude) {
        if(latitude == null || longitude == null) {
            return storeRepository.findByIsMainTrue().orElseThrow(() -> new EntityNotFoundException("Store not found"));
        }
        return storeRepository.findNearestStore(latitude, longitude);
    }

    @Override
    public StoreWithProductsDto getProductsFromNearestStore(Double latitude, Double longitude) {
        Store store;
        if(latitude == null || longitude == null) {
            store = storeRepository.findByIsMainTrue().orElseThrow(() -> new EntityNotFoundException("Store not found"));
        } else {
            store = storeRepository.findNearestStore(latitude, longitude);
        }

        StoreWithProductsDto storeDto = new StoreWithProductsDto();
        storeDto.setId(store.getId());

        Map<Long, Integer> stockMap = inventoryService.getStockByStore(store);

        List<ProductWithInventoryDto> products = productService.getAllProducts().stream().map(productDto -> {
            ProductWithInventoryDto product = new ProductWithInventoryDto();

            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());
            product.setImages(productDto.getImages());
            product.setCategoryId(productDto.getCategoryId());
            product.setStock(stockMap.getOrDefault(productDto.getId(), 0));

            return product;

        }).collect(Collectors.toList());

        storeDto.setProducts(products);

        return storeDto;

    }
}
