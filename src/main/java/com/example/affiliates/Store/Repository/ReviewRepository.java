package com.example.affiliates.Store.Repository;

import com.example.affiliates.Store.Entity.ReviewEntity;
import com.example.affiliates.Store.Entity.StoreEntity;
import com.example.affiliates.User.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByStoreIdxOrderByCreatedDate(StoreEntity storeEntity);
    List<ReviewEntity> findByUserIdxOrderByCreatedDate(UserEntity userEntity);
    List<ReviewEntity> findAllByUserIdx(UserEntity userEntity);
}
