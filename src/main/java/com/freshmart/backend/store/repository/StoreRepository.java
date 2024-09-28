package com.freshmart.backend.store.repository;

import com.freshmart.backend.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
