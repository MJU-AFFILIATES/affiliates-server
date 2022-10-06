package com.example.affiliates.User.Service;

import com.example.affiliates.Store.Entity.ReviewEntity;
import com.example.affiliates.Store.Repository.ReviewRepository;
import com.example.affiliates.User.DTO.UserDTO;
import com.example.affiliates.User.Entity.UserEntity;
import com.example.affiliates.User.Repository.UserRepository;
import com.example.affiliates.Util.*;
import com.example.affiliates.jwt.DTO.TokenDTO;
import com.example.affiliates.jwt.TokenProvider;
import com.example.affiliates.jwt.entity.RefreshTokenEntity;
import com.example.affiliates.jwt.repository.RefreshTokenRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.example.affiliates.Util.RegexPattern.isRegexPwd;

@Service
public class UserService {
    private UserRepository userRepository;
    private ReviewRepository reviewRepository;
    private RefreshTokenRepository refreshTokenRepository;
    private TokenProvider tokenProvider;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    private RedisTemplate redisTemplate;

    public UserService(UserRepository userRepository, ReviewRepository reviewRepository, TokenProvider tokenProvider, RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder, AuthenticationManagerBuilder authenticationManagerBuilder,
                       RedisTemplate redisTemplate){
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.redisTemplate = redisTemplate;
    }

    public TokenDTO login(UserDTO.Login user) throws BaseException{
        UserEntity opUser = this.checkHasUser(user.getStudentNum(), BaseResponseStatus.USER_POST_NOT_SIGN_IN);
        if(!passwordEncoder.matches(user.getPassword(), opUser.getPassword())) {
            throw new BaseException(BaseResponseStatus.USER_POST_NOT_SIGN_IN);
        }
        return token(user);
    }

    public TokenDTO token(UserDTO.Login user){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getStudentNum(), user.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDTO tokenDto = tokenProvider.generateTokenDto(authentication, authentication.getName());
        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);
        return tokenDto;
    }

    public TokenDTO signIn(UserDTO.Login user) throws BaseException {
        if(user.getStudentNum() == null || user.getNickName() == null || user.getPassword() == null){
            throw new BaseException(BaseResponseStatus.USER_DONOT_WRITE_INFO);
        }
        if(this.duplicateCheckNum(user.getStudentNum())){
            throw new BaseException(BaseResponseStatus.EXIST_USER_NUM);
        }
        if(this.duplicateCheckNickName(user.getNickName())){
            throw new BaseException(BaseResponseStatus.EXIST_NICKNAME);
        }
        String password = user.getPassword();
        if(!isRegexPwd(password)){
            throw new BaseException(BaseResponseStatus.REGEX_PWD);
        }
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
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken(), request)) {
            throw new BaseException(BaseResponseStatus.DO_NOT_HAVE_REFRESH);
        }
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByKeyId(authentication.getName())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.LOGOUT_USER));
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new BaseException(BaseResponseStatus.DO_NOT_HAVE_REFRESH);
        }
        // 5. 새로운 토큰 생성
        TokenDTO tokenDto = tokenProvider.generateTokenDto(authentication, authentication.getName());
        // 6. 저장소 정보 업데이트
        RefreshTokenEntity newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);
        // 토큰 발급
        return tokenDto;
    }

    public void changeNickName(Principal principal, UserDTO.NickName nickName) throws BaseException{
        UserEntity user = this.checkHasUser(principal.getName(), BaseResponseStatus.USER_POST_NOT_SIGN_IN);
        Boolean hasNickName = this.userRepository.existsByNickName(nickName.getNickName());
        if(nickName.getNickName().isEmpty()){
            throw new BaseException(BaseResponseStatus.DO_NOT_HAVE_NICKNAME);
        }
        if(nickName.getNickName().equals(user.getNickName())){
            throw new BaseException(BaseResponseStatus.SAME_NICKNAME);
        }
        if(hasNickName == true){
            throw new BaseException(BaseResponseStatus.EXIST_NICKNAME);
        }
        user.changeNickName(nickName.getNickName());
        userRepository.save(user);

    }

    public Boolean duplicateCheckNum(String studentNum) throws BaseException{
        return this.userRepository.existsByStudentNum(studentNum);
    }

    public Boolean duplicateCheckNickName(String nickName) throws BaseException{
        return this.userRepository.existsByNickName(nickName);
    }

    public UserEntity checkHasUser(String studentNum, BaseResponseStatus status) throws BaseException {
        return this.userRepository.findByStudentNum(studentNum)
                .orElseThrow(() -> new BaseException(status));
    }

    public void logout(Principal principal, HttpServletRequest request) throws BaseException{
        UserEntity user = this.checkHasUser(principal.getName(), BaseResponseStatus.NULL_JWT);
        String accessToken = request.getHeader("Authorization").substring(7);

        Long expiration = tokenProvider.getExpiration(accessToken);
        redisTemplate.opsForValue()
                .set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);

        user.changeLoginStatus(LoginStatus.LOG_OUT);
        this.userRepository.save(user);

        RefreshTokenEntity byKeyId = this.refreshTokenRepository.findByKeyId(user.getStudentNum())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.DO_NOT_HAVE_REFRESH));
        this.refreshTokenRepository.delete(byKeyId);
    }

    public void deleteUserData(Principal principal, HttpServletRequest request) throws BaseException{
        UserEntity userEntity = this.checkHasUser(principal.getName(), BaseResponseStatus.NULL_JWT);
        String accessToken = request.getHeader("Authorization").substring(7);
        Long expiration = tokenProvider.getExpiration(accessToken);
        redisTemplate.opsForValue()
                .set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
        String studNum = userEntity.getStudentNum();
        try{
            List<ReviewEntity> reviewEntityList = reviewRepository.findAllByUserIdx(userEntity);
            for(ReviewEntity i : reviewEntityList){
                userEntity.getReviews().remove(i);
                reviewRepository.delete(i);
            }
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DELETE_REVIEW_ERROR);
        }
        try{
            Optional<RefreshTokenEntity> op = refreshTokenRepository.findByKeyId(studNum);
            RefreshTokenEntity refreshTokenEntity = op.get();
            refreshTokenRepository.delete(refreshTokenEntity);
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DO_NOT_HAVE_REFRESH);
        }

        try{
            userRepository.deleteById(userEntity.getUserIdx());
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DELETE_USER_ERROR);
        }
    }

    public UserDTO.Mypage myPage(Principal principal) throws BaseException {
        UserEntity userEntity = this.checkHasUser(principal.getName(), BaseResponseStatus.NULL_JWT);
        return new UserDTO.Mypage(userEntity.getStudentNum(), userEntity.getNickName());

    }
}
