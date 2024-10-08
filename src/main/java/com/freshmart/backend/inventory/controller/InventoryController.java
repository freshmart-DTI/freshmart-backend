package com.freshmart.backend.inventory.controller;

import com.freshmart.backend.inventory.dto.InventoryDto;
import com.freshmart.backend.inventory.entity.Inventory;
import com.freshmart.backend.inventory.service.impl.InventoryServiceImpl;
import com.freshmart.backend.response.Response;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventories")
@Validated
public class InventoryController {
    private final InventoryServiceImpl inventoryService;

    public InventoryController(InventoryServiceImpl inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllInventories(@RequestParam(value = "storeId", required = false) Long storeId) {
        List<InventoryDto> inventories = inventoryService.getAllInventories(storeId);
        return Response.success("List of inventories fetched", inventories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInventoryById(@PathVariable("id") Long id) {
        InventoryDto inventoryDto = inventoryService.getInventoryById(id);
        return Response.success("Inventory with id: " +  id + " fetched", inventoryDto);
    }

    @PostMapping()
    public ResponseEntity<?> createInventory(@Valid @RequestBody InventoryDto inventoryDto) {
        Inventory inventory = inventoryService.createInventory(inventoryDto);
        return Response.success("Inventory created successfully", inventory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInventory(@Valid @PathVariable("id") Long id, @RequestBody InventoryDto inventoryDto) {
        Inventory inventory = inventoryService.updateInventory(id, inventoryDto);
        return Response.success("Inventory updated successfully", inventory);
    }

}
