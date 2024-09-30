package com.freshmart.backend.store.service;

import com.freshmart.backend.store.dto.StoreDto;
import com.freshmart.backend.store.entity.Store;

import java.util.List;

public interface StoreService {
    List<Store> getAllStores();
    Store createStore(StoreDto storeDto);
    Store getStoreById(Long storeId);
}
