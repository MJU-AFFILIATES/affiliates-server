package com.example.affiliates.User.Service;

import com.example.affiliates.User.DTO.UserDTO;
import com.example.affiliates.User.Entity.UserEntity;
import com.example.affiliates.User.Repository.UserRepository;
import com.example.affiliates.Util.BaseException;
import com.example.affiliates.Util.BaseResponseStatus;
import com.example.affiliates.Util.LoginStatus;
import com.example.affiliates.Util.Role;
import com.example.affiliates.jwt.DTO.TokenDTO;
import com.example.affiliates.jwt.TokenProvider;
import com.example.affiliates.jwt.entity.RefreshTokenEntity;
import com.example.affiliates.jwt.repository.RefreshTokenRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private RefreshTokenRepository refreshTokenRepository;
    private TokenProvider tokenProvider;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    public UserService(UserRepository userRepository, TokenProvider tokenProvider, RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder, AuthenticationManagerBuilder authenticationManagerBuilder){
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    public UserEntity login(UserDTO.Login user) throws BaseException{
        //에러 처리 필요
        UserEntity userEntity = this.userRepository.findByStudentNum(user.getStudentNum()).get();

        // user가 가입 되어 있는지 확인(ID가 맞는지도 확인)
        if(userEntity == null){
            throw new BaseException(BaseResponseStatus.USER_POST_NOT_SIGN_IN);
        }
        // PASSWORD 맞는지 확인
        if(!user.getPassword().equals(userEntity.getPassword())){
            throw new BaseException(BaseResponseStatus.USER_POST_NOT_SIGN_IN);
        }
        System.out.println(this.token(user));
        return userEntity;
    }

    public TokenDTO token(UserDTO.Login user){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getStudentNum(), user.getPassword());
        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDTO tokenDto = tokenProvider.generateTokenDto(authentication, authentication.getName());
        // 4. RefreshToken 저장
        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);
        // 5. 토큰 발급
        return tokenDto;
    }

    public TokenDTO signIn(UserDTO.Login user) throws BaseException {
        if(user.getStudentNum() == null || user.getNickName() == null || user.getPassword() == null){
            throw new BaseException(BaseResponseStatus.PASSWORD_ENCRYPTION_ERROR);
        }
        String password = user.getPassword();
        try{
            String encodedPwd = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPwd);
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.PASSWORD_ENCRYPTION_ERROR);
        }
        UserEntity userEntity = UserEntity.builder()
                .studentNum(user.getStudentNum())
                .password(user.getPassword())
                .nickName(user.getNickName())
                .loginStatus(LoginStatus.LOGIN)
                .role(Role.ROLE_USER)
                .build();
        user.setPassword(password);
        userRepository.save(userEntity);
        return token(user);

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
