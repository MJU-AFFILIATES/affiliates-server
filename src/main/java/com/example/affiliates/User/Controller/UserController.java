package com.example.affiliates.User.Controller;

import com.example.affiliates.User.DTO.UserDTO;
import com.example.affiliates.User.Entity.UserEntity;
import com.example.affiliates.User.Service.UserService;
import com.example.affiliates.Util.BaseException;
import com.example.affiliates.Util.BaseResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }
    @ResponseBody
    @PostMapping("/login")
    public  BaseResponse<UserEntity> Login(@RequestBody UserDTO.Login user){
        try {
            return new BaseResponse<>(this.userService.login(user));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/sign-in")
    public BaseResponse<String> signIn(@RequestBody UserDTO.Login user){
        try {
            this.userService.signIn(user);
            return new BaseResponse<>("회원가입에 성공했습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }
}
