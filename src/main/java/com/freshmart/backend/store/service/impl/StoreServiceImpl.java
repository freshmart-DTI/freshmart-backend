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
}
