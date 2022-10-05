package com.example.affiliates.User.Controller;


import com.example.affiliates.User.DTO.UserDTO;
import com.example.affiliates.User.Service.UserService;
import com.example.affiliates.Util.BaseException;
import com.example.affiliates.Util.BaseResponse;
import com.example.affiliates.jwt.DTO.TokenDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/auth")
// token 발급 없어도 접근이 가능한 api 저장
public class AuthController {
    private UserService userService;

    public AuthController(UserService userService){
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
    public BaseResponse<TokenDTO> Login(@RequestBody UserDTO.Login user){
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
            @ApiResponse(code = 2000, message = "이미 있는 사용자 학번입니다."),
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
    @ResponseBody
    @ApiOperation(value = "토큰 재발급 api")
    @ApiResponses(value = {
            @ApiResponse(code = 2015, message = "로그아웃 된 사용자입니다."),
            @ApiResponse(code = 2016, message = "User 정보가 일치하지 않습니다.")
    })
    @PostMapping("/reissue")
    public BaseResponse<TokenDTO> reissue(@RequestBody TokenDTO tokenRequestDto, HttpServletRequest request) {
        try {
            return new BaseResponse<>(userService.reissue(tokenRequestDto, request));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /*
     * 장채은: 학번 중복 체크
     * */

    @ResponseBody
    @ApiOperation(value = "학번 중복 체크 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
    })
    @PostMapping("/checkStudent")
    public BaseResponse<Boolean> DuplicateCheckNum(@RequestBody UserDTO.StudentNum studentNum) {
        try {
            return new BaseResponse<>(userService.duplicateCheckNum(studentNum.getStudentNum()));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /*
     * 장채은: 닉네임 중복 체크
     * */
    @ResponseBody
    @ApiOperation(value = "닉네임 중복 체크 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
    })
    @PostMapping("/checkNickName")
    public BaseResponse<Boolean> DuplicateCheckNickName(@RequestBody UserDTO.NickName nickName) {
        try {
            return new BaseResponse<>(userService.duplicateCheckNickName(nickName.getNickName()));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
