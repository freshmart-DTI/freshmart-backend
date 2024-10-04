package com.freshmart.backend.store.repository;

import com.freshmart.backend.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    @Query(value = "SELECT * FROM store " +
            "ORDER BY POWER(latitude - :lat, 2) + POWER(longitude - :lon, 2) ASC " +
            "LIMIT 1", nativeQuery = true)
    Store findNearestStore(@Param("lat") double latitude, @Param("lon") double longitude);

    Optional<Store> findByIsMainTrue();
}
