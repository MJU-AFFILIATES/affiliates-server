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
import com.example.affiliates.Util.CategoryEnum;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StoreService {
    private UserRepository userRepository;
    private StoreRepository storeRepository;
    private ReviewRepository reviewRepository;
    private final String apiKey;
    public StoreService(StoreRepository storeRepository, UserRepository userRepository, ReviewRepository reviewRepository,
                        @Value("${kakao.key}")String apiKey){
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.apiKey = apiKey;
    }

    public void storeReview(Principal principal, StoreDTO.StoreReview storeReview) throws BaseException{
        Optional<UserEntity> optional = this.userRepository.findByStudentNum(principal.getName());
        if(storeReview.getStoreIdx() == null){
            throw new BaseException(BaseResponseStatus.REVIEW_STOREID_EMPTY);
        }
        if(storeReview.getStar()<1 || storeReview.getStar()>5){
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

    public StoreDTO.Location getKakaoApiFromAddress(String roadFullAddr) throws BaseException{;
        String apiUrl = "https://dapi.kakao.com/v2/local/search/address.json";
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + apiKey);
        String body = "";
        String query = roadFullAddr;
        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        if(requestEntity == null){
            throw new BaseException(BaseResponseStatus.NULL_HEADER);
        }

        String url = apiUrl + "?query="  + query;
        ResponseEntity<String> responseEntity = rest.exchange(url, HttpMethod.GET, requestEntity, String.class);
        if(responseEntity == null){
            throw new BaseException(BaseResponseStatus.NULL_RESPONSE_ENTITY);
        }
        JSONObject rjson = new JSONObject(responseEntity.getBody());
        JSONObject documents = (JSONObject) rjson.getJSONArray("documents").get(0);
        JSONObject address = documents.getJSONObject("address");


        return new StoreDTO.Location(address.getString("x"), address.getString("y"));
    }

    public List<StoreDTO.UserReviewList> getUserReviewList(Principal principal) throws BaseException{
        Optional<UserEntity> optional = this.userRepository.findByStudentNum(principal.getName());
        List<ReviewEntity> reviewEntity = reviewRepository.findByUserIdxOrderByCreatedDate(optional.get());
        List<StoreDTO.UserReviewList> reviewList = new ArrayList<>();

        for(ReviewEntity i : reviewEntity){
            StoreDTO.UserReviewList review = new StoreDTO.UserReviewList();
            review.setStoreIdx(i.getStoreIdx().getStoreIdx());
            review.setName(i.getStoreIdx().getName());
            review.setCategory(i.getStoreIdx().getCategoryEnum());
            review.setNickName(optional.get().getNickName());
            review.setReview(i.getReview());
            review.setStar(i.getStar());
            review.setCreatedDate(i.getCreatedDate());
            reviewList.add(review);
        }
        return reviewList;
    }


    public List<StoreDTO.ReviewList> getReviewList(Long storeIdx) throws BaseException{

        StoreEntity storeEntity = storeRepository.findByStoreIdx(storeIdx);
        List<ReviewEntity> reviewEntity = reviewRepository.findByStoreIdxOrderByCreatedDate(storeEntity);
        List<StoreDTO.ReviewList> reviewList = new ArrayList<>();

        if(storeIdx == null){
            throw new BaseException(BaseResponseStatus.NULL_PATH);
        }

        double avg = 0;
        double sum = 0;
        for(int i=0; i<reviewEntity.size(); i++){
            sum += (double)reviewEntity.get(i).getStar();
        }
        avg = Double.parseDouble(String.format("%.2f", sum/(double)reviewEntity.size()));

        for(ReviewEntity i : reviewEntity){
            StoreDTO.ReviewList review = new StoreDTO.ReviewList();
            review.setReviewIdx(i.getReviewIdx());
            review.setStoreIdx(storeIdx);
            review.setName(storeEntity.getName());
            review.setCategory(storeEntity.getCategoryEnum());
            review.setAvgStar(avg);
            review.setUserIdx(i.getUserIdx().getUserIdx());
            review.setNickName(i.getUserIdx().getNickName());
            review.setReview(i.getReview());
            review.setStar(i.getStar());
            review.setImgUrl(i.getStoreIdx().getImgUrl());
            review.setImgUrl(i.getStoreIdx().getImgUrl());
            review.setCreatedDate(i.getCreatedDate());
            reviewList.add(review);
        }
        return reviewList;
    }


    public List<StoreDTO.StoreList> storeList(int category) throws BaseException{
        List<StoreEntity> store = null;
        if(category == 0){
            store = this.storeRepository.findAll();
        }else if(category == 1) {
            store = this.storeRepository.findByCategoryEnum(CategoryEnum.CAFE);
        }else if(category == 2){
            store = this.storeRepository.findByCategoryEnum(CategoryEnum.BAR);
        }else if(category == 3){
            store = this.storeRepository.findByCategoryEnum(CategoryEnum.RESTAURANT);
        }else if(category == 4) {
            store = this.storeRepository.findByCategoryEnum(CategoryEnum.ACTIVITY);
        }else if (store == null || category>4) {
           throw new BaseException(BaseResponseStatus.CAN_NOT_ACCESS_STORE_FROM_CATEGORY);
        }
        List<StoreDTO.StoreList> list = new ArrayList<>();
        for(StoreEntity storeEntity: store){
            StoreDTO.StoreList storeList = new StoreDTO.StoreList();
            storeList.setStoreIdx(storeEntity.getStoreIdx());
            storeList.setName(storeEntity.getName());
            storeList.setCategory(storeEntity.getCategoryEnum());
            storeList.setAddress(storeEntity.getAddress());
            storeList.setContents(storeEntity.getContents());
            StoreDTO.Location location = this.getKakaoApiFromAddress(storeEntity.getAddress());
            storeList.setX(location.getX());
            storeList.setY(location.getY());
            list.add(storeList);
        }
        return list;
    }
}
