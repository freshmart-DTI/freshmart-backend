package com.freshmart.backend.store.entity;

import com.freshmart.backend.inventory.entity.Inventory;
import com.freshmart.backend.order.entity.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "stores")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "store_id_gen")
    @SequenceGenerator(name = "store_id_gen", sequenceName = "store_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @NotBlank
    @Column(name = "province", nullable = false)
    private String province;

    @NotNull
    @NotBlank
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @NotBlank
    @Column(name = "district", nullable = false)
    private String district;

    @NotNull
    @Column(name = "latitude", nullable = false)
    private BigDecimal latitude;

    @NotNull
    @Column(name = "longitude", nullable = false)
    private BigDecimal longitude;

//    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Inventory> inventories;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StoreAdmin> storeAdmins = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;
}
