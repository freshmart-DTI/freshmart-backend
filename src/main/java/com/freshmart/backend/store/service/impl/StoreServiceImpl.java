package com.freshmart.backend.store.service.impl;

import com.freshmart.backend.store.dto.StoreDto;
import com.freshmart.backend.store.entity.Store;
import com.freshmart.backend.store.repository.StoreRepository;
import com.freshmart.backend.store.service.StoreService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;

    public StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public List<Store> getAllStores() {
        return List.of();
    }

    @Override
    public Store createStore(StoreDto storeDto) {
        Store store = storeDto.toEntity();

        return storeRepository.save(store);
    }

    @Override
    public Store getStoreById(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() -> new EntityNotFoundException("Store not found with id: " + storeId));
    }
}
