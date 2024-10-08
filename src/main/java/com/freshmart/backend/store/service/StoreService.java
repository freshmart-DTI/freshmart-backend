package com.freshmart.backend.store.service;

import com.freshmart.backend.store.dto.StoreDto;
import com.freshmart.backend.store.dto.StoreWithProductsDto;
import com.freshmart.backend.store.entity.Store;

import java.math.BigDecimal;
import java.util.List;

public interface StoreService {
    List<Store> getAllStores();
    Store createStore(StoreDto storeDto);
    Store getStoreById(Long storeId);
    Store findNearestStore(Double longitude, Double latitude);
    StoreWithProductsDto getProductsFromNearestStore(Double latitude, Double longitude);
}
