package com.freshmart.backend.store.entity;

import com.freshmart.backend.users.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
@Table(name = "store_admins", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "store_id"}))
public class StoreAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "store_admin_id_gen")
    @SequenceGenerator(name = "store_admin_id_gen", sequenceName = "store_admin_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "assigned_at", nullable = false)
    private Instant assignedAt;
}
