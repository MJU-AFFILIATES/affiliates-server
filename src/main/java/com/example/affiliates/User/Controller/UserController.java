package com.example.affiliates.User.Controller;

import com.example.affiliates.User.DTO.UserDTO;
import com.example.affiliates.User.Entity.UserEntity;
import com.example.affiliates.User.Service.UserService;
import com.example.affiliates.Util.BaseException;
import com.example.affiliates.Util.BaseResponse;
import com.example.affiliates.jwt.DTO.TokenDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RequestMapping(value = "/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    /*
     * 오수연: 닉네임 변경
     * */
    @ResponseBody
    @PatchMapping(value = "/changeNickName")
    public BaseResponse<String> changeNickName (Principal principal, @RequestBody UserDTO.NickName nickName){
        try {
            this.userService.changeNickName(principal, nickName);
            return new BaseResponse<>("닉네임을 변경했습니다.");
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


}
