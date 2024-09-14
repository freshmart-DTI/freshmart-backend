package com.freshmart.backend.inventory.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Data
@Table(name = "inventory_journals")
public class InventoryJournal {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventory_journal_id_gen")
    @SequenceGenerator(name = "inventory_journal_id_gen", sequenceName = "inventory_journal_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @Column(name = "quantity_change", nullable = false)
    private Integer quantityChange;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
