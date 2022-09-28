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
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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

    public StoreDTO.Location getKakaoApiFromAddress(String roadFullAddr) throws BaseException{;
        String apiUrl = "https://dapi.kakao.com/v2/local/search/address.json";
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + apiKey);
        String body = "";
        String query = roadFullAddr;
        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        if(requestEntity.equals(null)){
            throw new BaseException(BaseResponseStatus.NULL_RESPONSE_ENTITY);
        }

        String url = apiUrl + "?query="  + query;
        ResponseEntity<String> responseEntity = rest.exchange(url, HttpMethod.GET, requestEntity, String.class);
        if(responseEntity.equals(null)){
            throw new BaseException(BaseResponseStatus.NULL_RESPONSE_ENTITY);
        }
        JSONObject rjson = new JSONObject(responseEntity.getBody());
        JSONObject documents = (JSONObject) rjson.getJSONArray("documents").get(0);
        JSONObject address = documents.getJSONObject("address");


        return new StoreDTO.Location(address.getString("x"), address.getString("y"));
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
        }else if (store.equals(null) || category>4) {
            // exception 처리
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
