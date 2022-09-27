package com.example.affiliates.Store.Service;

import com.example.affiliates.Store.DTO.StoreDTO;
import com.example.affiliates.Store.Entity.ReviewEntity;
import com.example.affiliates.Store.Entity.StoreEntity;
import com.example.affiliates.Store.Repository.ReviewRepository;
import com.example.affiliates.Store.Repository.StoreRepository;
import com.example.affiliates.User.Entity.UserEntity;
import com.example.affiliates.User.Repository.UserRepository;
import com.example.affiliates.Util.BaseException;
import com.example.affiliates.Util.BaseResponseStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class StoreService {
    private UserRepository userRepository;
    private StoreRepository storeRepository;
    private ReviewRepository reviewRepository;
    public StoreService(StoreRepository storeRepository, UserRepository userRepository, ReviewRepository reviewRepository){
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    public void storeReview(Principal principal, StoreDTO.GetStoreReview storeReview) throws BaseException{
        Optional<UserEntity> optional = this.userRepository.findByStudentNum(principal.getName());
        if(storeReview.getStoreIdx() == null){
            throw new BaseException(BaseResponseStatus.REVIEW_STOREID_EMPTY);
        }
        if(storeReview.getStar()<=1 || storeReview.getStar()>=5){
            throw new BaseException(BaseResponseStatus.REVIEW_STAR_EMPTY);
        }
        StoreEntity store = this.storeRepository.findByStoreIdx(storeReview.getStoreIdx());
        ReviewEntity review = ReviewEntity.builder()
                .userIdx(optional.get())
                .storeIdx(store)
                .review(storeReview.getReview())
                .star(storeReview.getStar())
                .build();
        this.reviewRepository.save(review);
    }
}
