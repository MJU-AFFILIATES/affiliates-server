package com.example.affiliates.Util;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
    * [POST] 2000
    */
    USER_POST_SIGN_IN(false, 2000, "이미 있는 사용자 학번입니다."),
    USER_POST_NOT_SIGN_IN(false, 2001, "가입하지 않았거나, 아이디/비밀번호가 틀렸습니다."),
    USER_DONOT_WRITE_INFO(false, 2002,"정보를 작성하지 않았습니다."),
    EXIST_USER_NUM(false, 2003, "이미 등록된 학번 입니다."),
    EXIST_NICKNAME(false, 2004, "중복된 닉네임입니다."),
    REGEX_PWD(false, 2005, "비밀번호 양식에 맞게 변경해주세요."),

    DO_NOT_HAVE_NICKNAME(false, 2006, "변경할 닉네임을 입력해주세요."),
    SAME_NICKNAME(false, 2007, "동일한 닉네임으로 변경할 수 없습니다."),

    REVIEW_STOREID_EMPTY(false, 2008, "storeIdx가 공백입니다."),
    REVIEW_STAR_EMPTY(false, 2009, "별점을 입력해주세요."),
    NULL_RESPONSE_ENTITY(false, 2010, "kakao 지도 api를 불러올 수 없거나, 주소가 잘못되어 있습니다."),
    NULL_HEADER(false, 2011, "헤더에 정보 값이 없습니다."),
    /*
     *5000: database error
     */
    PASSWORD_ENCRYPTION_ERROR(false, 4001, "비밀번호 암호화에 실패했습니다."),


    /*
     * 9500 : jwt
     * */

    WRONG_JWT_SIGN_TOKEN(false, 9500, "잘못된 JWT 서명입니다."),
    EXPIRED_JWT_TOKEN(false, 9501, "만료된 JWT 토큰 입니다."),
    UNSUPPORTED_JWT_TOKEN(false, 9502, "지원되지 않는 JWT 토큰입니다."),
    WRONG_JWT_TOKEN(false, 9503, "JWT 토큰이 잘못되었습니다."),
    NULL_JWT(false,9504, "JWT의 값이 없습니다.");
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