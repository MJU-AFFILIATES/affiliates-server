package com.example.affiliates.User.Controller;

import com.example.affiliates.User.DTO.UserDTO;
import com.example.affiliates.User.Entity.UserEntity;
import com.example.affiliates.User.Service.UserService;
import com.example.affiliates.Util.BaseException;
import com.example.affiliates.Util.BaseResponse;
import com.example.affiliates.jwt.DTO.TokenDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    /*
    * 장채은 : 로그인
    * */
    @ResponseBody
    @PostMapping("/login")
    public  BaseResponse<TokenDTO> Login(@RequestBody UserDTO.Login user){
        try {
            return new BaseResponse<>(this.userService.login(user));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /*
    * 장채은: 회원가입
    * */
    @ResponseBody
    @PostMapping("/sign-in")
    public BaseResponse<TokenDTO> signIn(@RequestBody UserDTO.Login user){
        try {
            return new BaseResponse<>(this.userService.signIn(user));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
    
    /*
    * 장채은: 토큰 재발급
    * */
    @PostMapping("/reissue")
    public BaseResponse<TokenDTO> reissue(@RequestBody TokenDTO tokenRequestDto, HttpServletRequest request) {
        try {
            return new BaseResponse<TokenDTO>(userService.reissue(tokenRequestDto, request));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
