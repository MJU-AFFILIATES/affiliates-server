package com.example.affiliates.User.Service;

import com.example.affiliates.Jwt.DTO.TokenDTO;
import com.example.affiliates.Jwt.Entity.RefreshTokenEntity;
import com.example.affiliates.Jwt.Repository.RefreshTokenRepository;
import com.example.affiliates.Jwt.TokenProvider;
import com.example.affiliates.User.DTO.UserDTO;
import com.example.affiliates.User.Entity.UserEntity;
import com.example.affiliates.User.Repository.UserRepository;
import com.example.affiliates.Util.BaseException;
import com.example.affiliates.Util.BaseResponseStatus;
import com.example.affiliates.Util.LoginStatus;
import com.example.affiliates.Util.Role;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {
    private UserRepository userRepository;
    private TokenProvider tokenProvider;
    private RefreshTokenRepository refreshTokenRepository;
    public UserService(UserRepository userRepository, TokenProvider tokenProvider, RefreshTokenRepository refreshTokenRepository){
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public UserEntity login(UserDTO.Login user) throws BaseException{
        //에러 처리 필요
        UserEntity userEntity = this.userRepository.findByUserNum(user.getStudentNum()).get();

        // user가 가입 되어 있는지 확인(ID가 맞는지도 확인)
        if(userEntity == null){
            throw new BaseException(BaseResponseStatus.USER_POST_NOT_SIGN_IN);
        }
        // PASSWORD 맞는지 확인
        if(!user.getPassword().equals(userEntity.getPassword())){
            throw new BaseException(BaseResponseStatus.USER_POST_NOT_SIGN_IN);
        }
        return userEntity;
    }

    public void signIn(UserDTO.Login user) throws BaseException {
        //에러 처리 필요
        UserEntity userEntity = this.userRepository.findByUserNum(user.getStudentNum()).get();
        if(userEntity != null){
            throw new BaseException(BaseResponseStatus.USER_POST_SIGN_IN);
        }
        userEntity = UserEntity.builder()
                .userNum(user.getStudentNum())
                .password(user.getPassword())
                .loginStatus(LoginStatus.LOGIN)
                .role(Role.ROLE_USER)
                .build();

        this.userRepository.save(userEntity);
    }

    public TokenDTO reissue(TokenDTO tokenRequestDto, HttpServletRequest request) throws BaseException{
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken(), request)) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByKeyId(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDTO tokenDto = tokenProvider.generateTokenDto(authentication, authentication.getName());

        // 6. 저장소 정보 업데이트
        RefreshTokenEntity newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }
}
