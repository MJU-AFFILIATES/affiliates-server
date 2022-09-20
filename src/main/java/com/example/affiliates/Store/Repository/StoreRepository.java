package com.example.affiliates.Store.Repository;

import com.example.affiliates.Store.Entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
}
