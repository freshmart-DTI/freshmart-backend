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

import java.util.List;

@RestController
@RequestMapping("/api/v1/stores")
@Validated
public class StoreController {
    private final StoreServiceImpl storeService;

    public StoreController(StoreServiceImpl storeService) {
        this.storeService = storeService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllStores() {
        List<Store> stores = storeService.getAllStores();

        return Response.success("List of stores fetched", stores);
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
    public ResponseEntity<?> getProductsFromNearestStore(@RequestParam(required = false) Double latitude,
                                                         @RequestParam(required = false) Double longitude,
                                                         @RequestParam(required = false) String search,
                                                         @RequestParam(required = false) String category,
                                                         @RequestParam(required = false) Double minPrice,
                                                         @RequestParam(required = false) Double maxPrice,
                                                         @RequestParam(required = false, defaultValue = "name") String sortBy,
                                                         @RequestParam(required = false, defaultValue = "true") Boolean sortAsc,
                                                         @RequestParam(required = false, defaultValue = "0") int page,
    @RequestParam(required = false, defaultValue = "10") int size
    ) {
        StoreWithProductsDto storeWithProducts = storeService.getProductsFromNearestStore(
                latitude, longitude, search, category, minPrice, maxPrice, sortBy, sortAsc, page, size);

        return Response.success("Nearest store fetched", storeWithProducts);
    }
}
