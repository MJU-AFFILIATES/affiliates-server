package com.example.affiliates.jwt.exception;

import com.example.affiliates.Util.BaseResponse;
import com.example.affiliates.Util.BaseResponseStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String exception = (String) request.getAttribute("exception");
        BaseResponseStatus status;
        if(exception == null) {
            status = BaseResponseStatus.NULL_JWT;
            setResponse(response, status);
            return;
        }

        /**
         * 토큰이 만료된 경우 예외처리
         */
        if(exception.equals("ExpiredJwtException")) {
            status = BaseResponseStatus.EXPIRED_JWT_TOKEN;
            setResponse(response, status);
            return;
        }
        if(exception.equals("MalformedJwtException")) {
            status = BaseResponseStatus.WRONG_JWT_SIGN_TOKEN;
            setResponse(response, status);
            return;
        }
        if(exception.equals("UnsupportedJwtException")) {
            status = BaseResponseStatus.UNSUPPORTED_JWT_TOKEN;
            setResponse(response, status);
            return;
        }
        if(exception.equals("IllegalArgumentException")) {
            status = BaseResponseStatus.WRONG_JWT_TOKEN;
            setResponse(response, status);
            return;
        }
    }

    private void setResponse(HttpServletResponse response, BaseResponseStatus status) throws IOException {

        BaseResponse baseResponse = new BaseResponse(status);
        response.setContentType("application/json;charset=UTF-8");
        var writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(baseResponse));
        writer.flush();
    }
}
