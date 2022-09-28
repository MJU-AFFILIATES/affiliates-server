package com.example.affiliates.Store.Controller;

import com.example.affiliates.Store.DTO.StoreDTO;
import com.example.affiliates.Store.Service.StoreService;
import com.example.affiliates.User.DTO.UserDTO;
import com.example.affiliates.User.Service.UserService;
import com.example.affiliates.Util.BaseException;
import com.example.affiliates.Util.BaseResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(value = "/stores")
public class StoreController{
    private StoreService storeService;

    public StoreController(StoreService storeService){
        this.storeService = storeService;
    }

    /*
     * 오수연 : 리뷰작성
     * */
    @ResponseBody
    @ApiOperation(value = "리뷰작성 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2008, message = "storeIdx가 공백입니다."),
            @ApiResponse(code = 2009, message = "별점을 입력해주세요.")
    })
    @PostMapping("/review")
    public BaseResponse<String> storeReview(Principal principal, @RequestBody StoreDTO.StoreReview store){
        try {
            storeService.storeReview(principal, store);
            return new BaseResponse<>("리뷰 작성을 완료했습니다.");
        }catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /*
    * 장채은: 위도 경도 api
    * */
    @ResponseBody
    @ApiOperation(value = "위도 경도 api")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2010, message = "kakao 지도 api를 불러올 수 없거나, 주소가 잘못되어 있습니다."),
            @ApiResponse(code = 2011, message = "헤더에 정보 값이 없습니다.")
    })
    @GetMapping("/address")
    public BaseResponse<StoreDTO.Location> address(@RequestParam String address){
        try {
            return new BaseResponse<>(storeService.getKakaoApiFromAddress(address));
        }catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /*
     * 장채은: 카테고리 별 상점 리스트
     * */
    @ResponseBody
    @ApiOperation(value = "카테고리 별 상점 리스트 api")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2012, message = "카테고리 값이 null이거나, 있는 카테고리 값이 아닙니다."),
    })
    @GetMapping("/storeList/{category}")
    public BaseResponse<List<StoreDTO.StoreList>> storeList(@PathVariable("category") int category){
        try {
            return new BaseResponse<>(storeService.storeList(category));
        }catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /*
     * 오수연: 상점 별 리뷰 리스트
     * */
    @ResponseBody
    @ApiOperation(value = "가게별 리뷰 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다.")
    })
    @GetMapping("/review/{storeIdx}")
    public BaseResponse<List<StoreDTO.ReviewList>> getReview(@PathVariable("storeIdx") Long storeIdx){
        try{
            List<StoreDTO.ReviewList> getReviewList = storeService.getReviewList(storeIdx);
            return new BaseResponse<>(getReviewList);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

}

