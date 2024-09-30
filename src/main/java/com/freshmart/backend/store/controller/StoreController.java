package com.freshmart.backend.store.controller;

import com.freshmart.backend.response.Response;
import com.freshmart.backend.store.dto.StoreDto;
import com.freshmart.backend.store.entity.Store;
import com.freshmart.backend.store.service.impl.StoreServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stores")
@Validated
public class StoreController {
    private final StoreServiceImpl storeService;

    public StoreController(StoreServiceImpl storeService) {
        this.storeService = storeService;
    }

    @PostMapping()
    public ResponseEntity<?> createStore(@Valid @RequestBody StoreDto storeDto) {
        Store store = storeService.createStore(storeDto);

        return Response.success("Store created successfully", store);
    }
}
