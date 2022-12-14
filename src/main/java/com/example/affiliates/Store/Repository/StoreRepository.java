package com.example.affiliates.Store.Repository;

import com.example.affiliates.Store.DTO.StoreDTO;
import com.example.affiliates.Store.Entity.StoreEntity;
import com.example.affiliates.Util.CategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
    StoreEntity findByStoreIdx(Long storeIdx);

    List<StoreEntity> findByCategoryEnum(CategoryEnum category);
}
