package com.example.affiliates.Store.DTO;

import com.example.affiliates.Util.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

import java.time.LocalDateTime;

@Getter
@Setter
public class StoreDTO {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class StoreReview{
        private Long storeIdx;
        private String review;
        private int star;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Location{
        private String x;
        private String y;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class ReviewList{
        private Long reviewIdx;
        private Long storeIdx;
        private String name;
        private CategoryEnum category;
        private double AvgStar;
        private Long userIdx;
        private String nickName;
        private String review;
        private String imgUrl;
        private int star;
        private LocalDateTime createdDate;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class UserReviewList{
        private Long storeIdx;
        private String name;
        private CategoryEnum category;
        private String nickName;
        private String review;
        private int star;
        private String imgUrl;
        private LocalDateTime createdDate;
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class StoreList{
        private Long storeIdx;
        private String name;
        private String contents;
        private CategoryEnum category;
        private String address;
        private String x;
        private String y;
    }
}
