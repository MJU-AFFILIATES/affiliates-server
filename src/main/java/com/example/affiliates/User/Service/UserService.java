package com.example.affiliates.User.Service;

import com.example.affiliates.User.DTO.UserDTO;
import com.example.affiliates.User.Entity.UserEntity;
import com.example.affiliates.User.Repository.UserRepository;
import com.example.affiliates.Util.BaseException;
import com.example.affiliates.Util.BaseResponseStatus;
import com.example.affiliates.Util.LoginStatus;
import com.example.affiliates.Util.Role;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
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
}
