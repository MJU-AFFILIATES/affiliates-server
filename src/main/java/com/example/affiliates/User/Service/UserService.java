package com.example.affiliates.User.Service;

import com.example.affiliates.User.DTO.UserDTO;
import com.example.affiliates.User.Entity.UserEntity;
import com.example.affiliates.User.Repository.UserRepository;
import com.example.affiliates.Util.BaseException;
import com.example.affiliates.Util.BaseResponseStatus;
import com.example.affiliates.Util.LoginStatus;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserEntity login(UserDTO.Login user) throws BaseException{
        UserEntity userEntity = this.userRepository.findByUserNum(user.getStudentNum());

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
        UserEntity userEntity = this.userRepository.findByUserNum(user.getStudentNum());
        if(userEntity != null){
            throw new BaseException(BaseResponseStatus.USER_POST_SIGN_IN);
        }
        userEntity = UserEntity.builder()
                .userNum(user.getStudentNum())
                .password(user.getPassword())
                .loginStatus(LoginStatus.LOGIN)
                .build();

        this.userRepository.save(userEntity);
    }
}
