package com.example.affiliates.User.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Login{
        @ApiModelProperty(value = "학번", example = "60200000")
        private String studentNum;
        @ApiModelProperty(value = "닉네임", example = "안녕")
        private String nickName;
        @ApiModelProperty(value = "비밀번호", example = "asfasdf123!")
        private String password;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class NickName{
        @ApiModelProperty(value = "닉네임", example = "안녕")
        private String nickName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class StudentNum{
        @ApiModelProperty(value = "학번", example = "60200000")
        private String studentNum;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Mypage{
        @ApiModelProperty(value = "학번", example = "60200000")
        private String studentNum;
        @ApiModelProperty(value = "닉네임", example = "안녕")
        private String nickName;
    }
}
