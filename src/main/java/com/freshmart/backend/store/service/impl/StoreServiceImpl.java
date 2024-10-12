package com.freshmart.backend.store.service.impl;

import com.freshmart.backend.inventory.entity.Inventory;
import com.freshmart.backend.inventory.service.impl.InventoryServiceImpl;
import com.freshmart.backend.product.dto.ProductDto;
import com.freshmart.backend.product.dto.ProductImageDto;
import com.freshmart.backend.product.dto.ProductWithInventoryDto;
import com.freshmart.backend.product.entity.Product;
import com.freshmart.backend.product.entity.ProductImage;
import com.freshmart.backend.product.repository.ProductSpecification;
import com.freshmart.backend.product.service.impl.ImageUploadServiceImpl;
import com.freshmart.backend.product.service.impl.ProductServiceImpl;
import com.freshmart.backend.response.PagedResponse;
import com.freshmart.backend.store.dto.StoreDto;
import com.freshmart.backend.store.dto.StoreWithProductsDto;
import com.freshmart.backend.store.entity.Store;
import com.freshmart.backend.store.repository.StoreRepository;
import com.freshmart.backend.store.service.StoreService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;
    private final ProductServiceImpl productService;
    private final InventoryServiceImpl inventoryService;
    private final ImageUploadServiceImpl imageUploadService;

    public StoreServiceImpl(StoreRepository storeRepository, ProductServiceImpl productService, InventoryServiceImpl inventoryService, ImageUploadServiceImpl imageUploadService) {
        this.storeRepository = storeRepository;
        this.productService = productService;
        this.inventoryService = inventoryService;
        this.imageUploadService = imageUploadService;
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
    public StoreWithProductsDto getProductsFromNearestStore(Double latitude, Double longitude,
                                                            String search, String category, Double minPrice, Double maxPrice, String sortBy, Boolean sortAsc, int page, int size) {
        Store store;
        if(latitude == null || longitude == null) {
            store = storeRepository.findByIsMainTrue().orElseThrow(() -> new EntityNotFoundException("Store not found"));
        } else {
            store = storeRepository.findNearestStore(latitude, longitude);
        }

        StoreWithProductsDto storeDto = new StoreWithProductsDto();
        storeDto.setId(store.getId());

        Map<Long, Integer> stockMap = inventoryService.getStockByStore(store);

        PagedResponse<Product> productPage = productService.getFilteredProducts(search, category, minPrice, maxPrice, sortBy, sortAsc, page, size);

        List<ProductWithInventoryDto> productDtos = productPage.getContent().stream().map(product -> {
            ProductWithInventoryDto dto = new ProductWithInventoryDto();

            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setPrice(product.getPrice());
            dto.setDescription(product.getDescription());
            dto.setImages(mapProductImagesToImageDtos(product.getImages()));
            dto.setCategoryId(product.getCategory().getId());
            dto.setStock(stockMap.getOrDefault(product.getId(), 0));

            return dto;

        }).collect(Collectors.toList());

        storeDto.setProducts(productDtos);

        return storeDto;

    }

    private List<ProductImageDto> mapProductImagesToImageDtos(List<ProductImage> productImages) {
        return productImages.stream().map(image -> {
            ProductImageDto imageDto = new ProductImageDto();
            imageDto.setId(image.getId());

            String imageUrl = imageUploadService.generateUrl(image.getUrl());
            imageDto.setUrl(imageUrl);
            return imageDto;
        }).collect(Collectors.toList());
    }
}
