package com.example.affiliates.Util;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
    * [POST] 2000
    */
    USER_POST_SIGN_IN(false, 2000, "이미 있는 사용자 학번입니다."),
    USER_POST_NOT_SIGN_IN(false, 2001, "가입하지 않은 사용자 학번입니다."),
    USER_PASSWORD_NOT_EQUAL(false, 2002, "틀린 비밀번호 입니다.")
    ;


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}