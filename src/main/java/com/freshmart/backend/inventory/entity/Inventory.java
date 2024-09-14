package com.freshmart.backend.inventory.entity;

import com.freshmart.backend.inventory.dto.InventoryDto;
import com.freshmart.backend.product.entity.Product;
import com.freshmart.backend.store.entity.Store;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Data
@Table(name = "inventories")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventory_id_gen")
    @SequenceGenerator(name = "inventory_id_gen", sequenceName = "inventory_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    private Integer quantity;

    public InventoryDto toDto() {
        InventoryDto inventoryDto = new InventoryDto();

        inventoryDto.setId(id);
        inventoryDto.setQuantity(quantity);
        inventoryDto.setProductId(product.getId());
        inventoryDto.setStoreId(storeId);

        return inventoryDto;
    }
}
