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
    * 장채은 : 로그인
    * */
    @ResponseBody
    @ApiOperation(value = "로그인 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2001, message = "가입하지 않았거나, 아이디/비밀번호가 틀렸습니다.")
    })
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
    @ApiOperation(value = "회원가입 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2000, message = "이미 있는 사용자 학번입니다.."),
            @ApiResponse(code = 2002, message = "정보를 작성하지 않았습니다."),
            @ApiResponse(code = 2003, message = "이미 등록된 학번 입니다."),
            @ApiResponse(code = 4001, message = "비밀번호 암호화에 실패했습니다."),
            @ApiResponse(code = 2005, message = "비밀번호 양식에 맞게 변경해주세요."),
            @ApiResponse(code = 2004, message = "중복된 닉네임입니다.")
    })
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
