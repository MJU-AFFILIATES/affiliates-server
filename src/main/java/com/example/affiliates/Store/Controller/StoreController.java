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

    @ResponseBody
    @PostMapping("/address")
    public BaseResponse<StoreDTO.Location> address(@RequestParam String address){
        try {
            return new BaseResponse<>(storeService.getKakaoApiFromAddress(address));
        }catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

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

