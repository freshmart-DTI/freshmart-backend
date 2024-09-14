package com.freshmart.backend.inventory.dto;

import com.freshmart.backend.inventory.entity.Inventory;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryDto {
    private Long id;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Store ID is required")
    private Long storeId;

    @NotNull(message = "Quantity is required")
    private Integer quantity;


}
