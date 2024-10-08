package com.freshmart.backend.store.controller;

import com.freshmart.backend.response.Response;
import com.freshmart.backend.store.dto.StoreDto;
import com.freshmart.backend.store.dto.StoreWithProductsDto;
import com.freshmart.backend.store.entity.Store;
import com.freshmart.backend.store.service.impl.StoreServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/nearest")
    public ResponseEntity<?> getNearestStore(@RequestParam(name = "lat", required = false) Double latitude, @RequestParam(name = "lon", required = false) Double longitude) {
        Store store = storeService.findNearestStore(latitude, longitude);

        return Response.success("Nearest store fetched", store);
    }

    @GetMapping("/nearest/products")
    public ResponseEntity<?> getProductsFromNearestStore(@RequestParam(name = "lat", required = false) Double latitude, @RequestParam(name = "lon", required = false) Double longitude) {
        StoreWithProductsDto store = storeService.getProductsFromNearestStore(latitude, longitude);

        return Response.success("Nearest store fetched", store);
    }
}
